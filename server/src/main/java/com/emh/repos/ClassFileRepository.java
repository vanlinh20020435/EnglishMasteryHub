package com.emh.repos;

import com.emh.entity.ClassFile;
import com.emh.entity.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ClassFileRepository extends JpaRepository<ClassFile, String>
{
	List<ClassFile> findAllByClasss(Classes classs);

	@Query("select c from ClassFile c where c.id = ?2 and c.classs = ?1")
	ClassFile findOneByClasssAndId(Classes classs, Integer id);
}
