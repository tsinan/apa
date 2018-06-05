package com.x.apa.loader.service;

import java.util.List;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.loader.data.ThreatActor;

/**
 * @author liumeng
 */
public interface ThreatActorService {
	
	List<ThreatActor> queryThreatActors(boolean openedOnly);

	Page<ThreatActor> queryThreatActors(PageRequest page);

	ThreatActor queryThreatActor(String id);

	ThreatActor createThreatActor(ThreatActor threatActor);

	int updateThreatActor(ThreatActor threatActor);
	
	int changeThreatActorStatus(String id, int status);

	int deleteThreatActor(String id);
}
