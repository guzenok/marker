package marker;

import org.springframework.data.repository.CrudRepository;

import marker.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called sheetRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Long> {
	
	public User findOneByPhone(String phone);
	
}
