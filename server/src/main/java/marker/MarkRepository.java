package marker;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called sheetRepository
// CRUD refers Create, Read, Update, Delete

public interface MarkRepository extends CrudRepository<Mark, Long> {
	
	public List<Mark> findBySheet_Id(Long sheet_id);
	
	@Query("SELECT m FROM Mark m WHERE m.sheet.id = :sheet AND m.author.id = :author AND m.deleted IS NULL")
	public List<Mark> findByAuthor_IdAndSheet_Id(@Param("sheet") Long sheet_id, @Param("author") Long author_id);
	
}
