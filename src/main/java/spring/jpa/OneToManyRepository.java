package spring.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface OneToManyRepository extends CrudRepository<OneToManyEntity, Integer> {
}
