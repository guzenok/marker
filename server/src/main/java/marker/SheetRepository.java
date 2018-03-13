package marker;

import org.springframework.data.repository.CrudRepository;


import java.util.List;
import marker.Sheet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called sheetRepository
// CRUD refers Create, Read, Update, Delete

public interface SheetRepository extends CrudRepository<Sheet, Long> {
	
	public List<Sheet> findByAuthor_Id(Long user_id);
	public List<Sheet> findByNameContaining(String name);
	
	@Modifying  
	@Transactional
	public Integer deleteByIdAndAuthor_Id(Long sheet_id, Long user_id);
	
	@Query("SELECT s FROM Sheet s WHERE s.author.id!=:author_id AND exists( SELECT 1 FROM Mark m WHERE m.sheet.id=s.id AND m.author.id=:author_id)")
	public List<Sheet> findByMarksAuthor_Id(@Param("author_id") Long user_id);
}
