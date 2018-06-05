package com.x.apa.common.web.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

/**
 * @author liumeng
 */
@Order(1)
@WebFilter(filterName = "menuFilter", urlPatterns = "/webpage/*")
public class MenuFilter implements Filter {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public static final String MENU_DATA_KEY = "appmenus";
	public static final String CURRENT_MENU_REQUEST_KEY = "current_menu";

	// URL = http://ip:port/context-name/webpage/module-name/...
	private static final String URL_MODULE_PREFIX = "/webpage";
	private static final String URI_PLACE_HOLDER = "#";

	@Value("classpath:menu/apa.json")
	private Resource menuResource;

	// 换成一个访问URL以及他所对应的Path
	private Map<String, String> cacheUriPathMapping = new HashMap<String, String>();

	// 保存一个模块的所有菜单Path（二级菜单path）
	private Map<String, List<String>> menuPathsMapping = new HashMap<String, List<String>>();

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig chain) throws ServletException {

		// 文件不存在或不可读，不初始化菜单
		if (menuResource == null || !menuResource.isReadable()) {
			logger.debug("no need build menu. no menu.json was found.");
			return;
		}

		// 取菜单文件名作为模块名
		String fileName = menuResource.getFilename();
		String currentModule = fileName.substring(fileName.lastIndexOf('/') + 1, fileName.indexOf('.'));

		// 从文件中解析菜单
		MenuList menus = new MenuList();
		try {
			InputStream in = menuResource.getInputStream();
			if (in != null) {
				ObjectMapper mapper = new ObjectMapper();
				menus = mapper.readValue(in, MenuList.class);
				in.close();
			} else {
				logger.warn("unable to open menu file .");
			}
		} catch (IOException e) {
			logger.warn("unable to open menu file.", e);
		}

		logger.debug("build system menu success! the menu is {}.", menusToString(menus));

		// 菜单列表放入Context供doFilter使用
		chain.getServletContext().setAttribute(MENU_DATA_KEY + currentModule, menus);

		// 构造二级菜单Path列表供doFilter使用
		List<String> modulePaths = new ArrayList<String>();
		for (Menu menu : menus) {
			if (CollectionUtils.isEmpty(menu.getChildren())) {
				continue;
			}
			for (Menu childMenu : menu.getChildren()) {
				modulePaths.add(childMenu.getPath());
				// 为菜单添加默认权限
				if (StringUtils.isBlank(childMenu.getPermissions())) {
					childMenu.setPermissions("PERM_USER");
				}
			}
		}
		Collections.sort(modulePaths, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.length() > o2.length() ? -1 : 1;
			}
		});
		menuPathsMapping.put(currentModule, modulePaths);
		logger.debug("map menu and path success! the menuPathsMapping is {}.", menuPathsMapping);
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			boolean hasMenu = false;

			HttpServletRequest req = (HttpServletRequest) request;
			ServletContext application = req.getSession().getServletContext();

			// 过滤不需要菜单的页面
			String nomenu = req.getParameter("nomenu");
			String unwrap = req.getParameter("unwrap");
			if (!BooleanUtils.toBoolean(nomenu) && !BooleanUtils.toBoolean(unwrap)) {

				String relativeURI = buildRelativeURI(req);
				if (relativeURI.length() > 1) {// 直接访问根路径不构造菜单
					String currentModule = findModuleNameByUri(req.getRequestURI(), req.getContextPath(),
							URL_MODULE_PREFIX);

					logger.debug("current request module is {}.", currentModule);

					MenuList menus = (MenuList) application.getAttribute(MENU_DATA_KEY + currentModule);
					logger.debug("application scope menu is:{}", menusToString(menus));

					if (CollectionUtils.isNotEmpty(menus)) {
						List<Menu> currentMenus = initCurrentMenus(menus, relativeURI, currentModule, application);
						hasMenu = currentMenus != null && currentMenus.size() > 0;
						req.setAttribute(CURRENT_MENU_REQUEST_KEY, currentMenus);
					}
				}
			}
			req.setAttribute("hasMenu", hasMenu);
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// do nothing
	}

	/**
	 * 打印输出菜单
	 * 
	 * @param menus
	 * @return
	 */
	private String menusToString(List<Menu> menus) {
		if (CollectionUtils.isEmpty(menus)) {
			return "\n";
		}

		StringBuilder sb = new StringBuilder("\n");
		for (Menu root : menus) {
			sb.append("text:").append(root.getText()).append("\n");
			if (CollectionUtils.isEmpty(root.getChildren())) {
				continue;
			}
			for (Menu child : root.getChildren()) {
				sb.append("\ttext:").append(child.getText()).append("\n");
			}
		}
		return sb.toString();
	}

	/**
	 * 去掉contextPath
	 * 
	 * @param req
	 * @return
	 */
	private String buildRelativeURI(HttpServletRequest req) {
		String uri = req.getRequestURI().replace('\\', '/');
		try {
			uri = URLDecoder.decode(uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.warn("url decode error.", e);
		}
		String contentPath = req.getContextPath();
		return contentPath.length() == 1 ? uri : uri.substring(contentPath.length());// contentPath
	}

	/**
	 * 去除URL中模块名前面的前缀例如webpage
	 * 
	 * @param uri
	 * @param contextPath
	 * @param fixedPrefix
	 * @return
	 */
	private String findModuleNameByUri(String uri, String contextPath, String... fixedPrefix) {
		uri = uri.replaceAll("\\\\", "/");
		if (!"/".equals(contextPath)) {
			uri = uri.substring(contextPath.length());
		}
		if (fixedPrefix != null && fixedPrefix.length > 0 && fixedPrefix[0].length() > 0
				&& uri.startsWith(fixedPrefix[0])) {
			uri = uri.substring(fixedPrefix[0].length());
		}
		uri = uri.startsWith("/") ? uri.substring(1) : uri;// 去掉‘/’
		return uri.indexOf('/') == -1 ? "" : uri.substring(0, uri.indexOf('/'));// 截取模块根目录
	}

	/**
	 * 初始化当前URL对应的菜单
	 * 
	 * @param allMenus
	 * @param uri
	 * @param currentModule
	 * @param context
	 * @return
	 */
	private List<Menu> initCurrentMenus(MenuList allMenus, String uri, String currentModule, ServletContext context) {
		List<Menu> currentMenus = Lists.newArrayList();
		String requestMenuPath = findMatchedPath(uri, currentModule);
		if (StringUtils.isBlank(requestMenuPath)) {
			return currentMenus;
		}

		// 提取当前URI的占位符取值
		String placeHoderValue = getPlaceHolderValue(requestMenuPath, uri);
		for (Menu m : allMenus) {
			Menu t = new Menu(m.getName(), m.getText(), m.getPath(), m.getUrl());
			for (Menu m1 : m.getChildren()) {
				// 将带有占位符的url替换为真实uri
				String realUri = dealPalaceHolderForPathByValue(m1.getUrl(), placeHoderValue);
				// 判断当前用户是否有操作该菜单的权限
				if (decidePermission(realUri, m1.getPermissions(), m1.getDecider(), context)) {
					Menu t1 = new Menu(m1.getName(), m1.getText(), m1.getPath(), realUri);
					if (requestMenuPath.equals(m1.getPath())) {
						t.setSelected(true);
						t1.setSelected(true);
					}
					t.getChildren().add(t1);
					if (StringUtils.isBlank(t.getUrl())) {
						t.setUrl(realUri);
					}
				}
			}
			if (CollectionUtils.isNotEmpty(t.getChildren())) {
				currentMenus.add(t);
			}
		}
		logger.debug("init menu for current user success. menu count is: {}.", currentMenus.size());
		return currentMenus;

	}

	/**
	 * 根据访问URI，获取对应的path
	 * 
	 * @param uri
	 * @param currentModule
	 * @return
	 */
	private String findMatchedPath(String uri, String currentModule) {
		List<String> pathsOfModule = menuPathsMapping.get(currentModule);
		if (CollectionUtils.isEmpty(pathsOfModule)) {
			return "";
		}
		String cachedPath = cacheUriPathMapping.get(uri);
		if (StringUtils.isNotBlank(cachedPath)) {
			return cachedPath;
		}

		for (int i = 0; i < pathsOfModule.size(); i++) {
			if (decisionOfPathUriMatch(pathsOfModule.get(i), uri, false)) {
				cacheUriPathMapping.put(uri, pathsOfModule.get(i));
				return pathsOfModule.get(i);
			}
		}
		return "";
	}

	/**
	 * 判断实际访问的URL与菜单Path是否匹配
	 * 
	 * @param path
	 * @param uri
	 * @param fullMatch
	 * @return
	 */
	private boolean decisionOfPathUriMatch(String path, String uri, boolean fullMatch) {
		path = dealPalaceHolderForPathByUri(path, uri);
		if (fullMatch) {
			return path.equals(uri);
		} else {
			return uri.startsWith(path);
		}
	}

	/**
	 * 
	 * @param path
	 * @param uri
	 * @return
	 */
	private String dealPalaceHolderForPathByUri(String path, String uri) {
		int palace = path.indexOf(URI_PLACE_HOLDER);
		if (palace != -1 && uri.length() > palace) {
			String pathPrefix = path.substring(0, palace);
			if (uri.startsWith(pathPrefix)) {
				String subfix = uri.substring(palace);
				String holderValue = subfix.substring(0, subfix.indexOf('/'));
				path = path.replaceAll(URI_PLACE_HOLDER, holderValue);
			}
		}

		return path;
	}

	/**
	 * 查询占位符信息
	 * 
	 * @param pathWithHolder
	 * @param uri
	 * @return
	 */
	private String getPlaceHolderValue(String pathWithHolder, String uri) {
		String holderValue = "";
		int holderIdx = pathWithHolder.indexOf(URI_PLACE_HOLDER);
		if (holderIdx != -1 && uri.length() > holderIdx) {
			String subfix = uri.substring(holderIdx);
			holderValue = subfix.substring(0, subfix.indexOf('/'));
		}
		return holderValue;
	}

	private String dealPalaceHolderForPathByValue(String path, String value) {
		path = path.replaceAll(URI_PLACE_HOLDER, value);
		return path;
	}

	private boolean decidePermission(String uri, String permissions, String decider, ServletContext context) {
		// User user = SpringSecurityUtils.getCurrentUser();
		// if (decider != null && context != null) {
		// WebApplicationContext beanContext =
		// WebApplicationContextUtils.getWebApplicationContext(context);
		// MenuPermissionDeciders d = beanContext.getBean(decider,
		// MenuPermissionDeciders.class);
		// if (d != null) {
		// return d.decision(uri);
		// }
		// } else if (permissions != null && user != null) {
		// String[] ps = permissions.split(",");
		// logger.debug("\"{}\" required permission is:{}", new Object[] { uri,
		// ps });
		// logger.debug("current user has permission is:{}",
		// user.getAuthorities());
		// return SpringSecurityUtils.hasAnyAuthorities(user, ps);
		// }
		// return false;
		return true;
	}

	public static class Menu {
		private String name;
		private String text;
		private String path;
		private String url;
		private String decider;
		private String permissions;
		private boolean selected = false;
		private List<Menu> children = new ArrayList<Menu>(0);

		public Menu() {
			super();
		}

		public Menu(String name, String text, String path, String url) {
			super();
			this.name = name;
			this.text = text;
			this.path = path;
			this.url = url;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public List<Menu> getChildren() {
			return children;
		}

		public List<Menu> addChild(Menu menu) {
			this.children.add(menu);
			return children;
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getPermissions() {
			return permissions;
		}

		public void setPermissions(String permissions) {
			this.permissions = permissions;
		}

		public String getDecider() {
			return decider;
		}

		public void setDecider(String decider) {
			this.decider = decider;
		}

	}

	public static interface MenuPermissionDeciders {
		boolean decision(String url);
	}

	public static class MenuList extends ArrayList<Menu> {
		private static final long serialVersionUID = 1L;
	}

}
