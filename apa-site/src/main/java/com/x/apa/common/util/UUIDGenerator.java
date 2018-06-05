package com.x.apa.common.util;

import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

/**
 * 自定义UUID主键生成策略，利用JDK的UUID生成之后再用BASE64压缩成22位长，再加上model自身的前缀标识（6位），共28位长
 */
public class UUIDGenerator {

	/**
	 * 直接返回格式化之后的UniqeID（前缀+UUID进行base64压缩后的串）
	 * 
	 * @param prefix
	 * @return
	 */
	public static String generateUniqueID(String prefix) {
		if (prefix == null)
			return compressedUUID(UUID.randomUUID());
		else {
			prefix += "_";
			return prefix + compressedUUID(UUID.randomUUID());
		}
	}

	private static String compressedUUID(UUID uuid) {
		byte[] byUuid = new byte[16];
		long least = uuid.getLeastSignificantBits();
		long most = uuid.getMostSignificantBits();
		long2bytes(most, byUuid, 0);
		long2bytes(least, byUuid, 8);
		String compressUUID = Base64.encodeBase64URLSafeString(byUuid);
		return compressUUID;
	}

	private static void long2bytes(long value, byte[] bytes, int offset) {
		for (int i = 7; i > -1; i--) {
			bytes[offset++] = (byte) ((value >> 8 * i) & 0xFF);
		}
	}

}
