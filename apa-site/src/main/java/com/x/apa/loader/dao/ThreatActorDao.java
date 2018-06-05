package com.x.apa.loader.dao;

import java.util.List;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.loader.data.ThreatActor;

/**
 * @author liumeng
 */
public interface ThreatActorDao {

	Page<ThreatActor> queryThreatActors(PageRequest page);

	List<ThreatActor> queryThreatActors(boolean openedOnly);

	ThreatActor queryThreatActor(String id);

	ThreatActor queryThreatActorByIpAndEmail(String ip, String email);

	ThreatActor saveThreatActor(ThreatActor threatActor);

	int updateThreatActor(ThreatActor threatActor);

	int changeThreatActorStatus(String id, int status);

	int deleteThreatActor(String id);
}
