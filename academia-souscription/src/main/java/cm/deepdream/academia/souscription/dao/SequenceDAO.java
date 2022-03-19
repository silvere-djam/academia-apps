package cm.deepdream.academia.souscription.dao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Component;

import cm.deepdream.academia.souscription.model.Etablissement;
import cm.deepdream.academia.souscription.model.Sequence;
import cm.deepdream.academia.souscription.model.SequenceGlobale;

@Component
public class SequenceDAO {
	@PersistenceContext
	private EntityManager entityManager ;
	
	public Long nextId(Etablissement etablissement, String className) throws Exception {
		String qs = "select s from Sequence s where s.etablissement=:etablissement and s.classname=:classname" ;
		Query query = entityManager.createQuery(qs) ;
		query.setParameter("classname", className) ;
		query.setParameter("etablissement", etablissement) ;
		List<Sequence> seqs = query.getResultList() ;
		if(seqs.size() == 0 ) {
			Sequence seq = new Sequence() ;
			seq.setId(System.currentTimeMillis());
			seq.setClassname(className);
			seq.setEtablissement(etablissement);
			seq.setKey(100000L);
			entityManager.persist(seq) ;
			return seq.getKey() ;
		} else {
			Sequence seq = seqs.get(0) ;
			seq.setKey(seq.getKey() + 1);
			seq.setEtablissement(etablissement);
			entityManager.merge(seq);
			return seq.getKey() ;
		}
	}
	
	
	
	public Long nextGlobalId(String className) throws Exception {
		String qs = "select s from SequenceGlobale s where  s.classname=:classname" ;
		Query query = entityManager.createQuery(qs) ;
		query.setParameter("classname", className) ;
		List<SequenceGlobale> seqs = query.getResultList() ;
		if(seqs.size() == 0 ) {
			SequenceGlobale seq = new SequenceGlobale() ;
			seq.setId(System.currentTimeMillis());
			seq.setClassname(className);
			seq.setKey(100000L);
			entityManager.persist(seq) ;
			return seq.getKey() ;
		} else {
			SequenceGlobale seq = seqs.get(0) ;
			seq.setKey(seq.getKey() + 1);
			entityManager.merge(seq);
			return seq.getKey() ;
		}
	}
	
}
