package spring.jpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@DataJpaTest
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AuditingSpec extends Specification {
    @Autowired
    OneToManyRepository repo

    def "New parent and child are both assigned IDs and dates"() {
        given:
            def parent = new OneToManyEntity()
            def child = new ManyToOneEntity()
            parent.setChildren([child])
//  Manually setting the parent on the child is a workaround for the @SpringBootTest.
//  It is not necessary for the @DataJpaTest
//            child.setParent(parent)
        when:
            def persisted = repo.save(parent)
        then:
            def persistedChild = persisted.children.first()
            persisted.createdDate
            persisted.createdDate == persisted.lastModifiedDate
            persistedChild.createdDate
            persistedChild.createdDate == persistedChild.lastModifiedDate
    }

    def "Appended child is assigned IDs and dates"() {
        given:
            def parent = new OneToManyEntity()
            parent.setChildren([new ManyToOneEntity()])
            def persisted = repo.save(parent)
            persisted.children.add(new ManyToOneEntity())
        when:
            def persisted2 = repo.save(persisted)
        then:
            persisted2.children.size() == 2
            def firstChild = persisted2.children.first()
            def secondChild = persisted2.children.last()
            secondChild.id > firstChild.id
            secondChild.createdDate
            secondChild.createdDate == secondChild.lastModifiedDate
    }

    /** This test fails when the class is annotated with {@code @DataJpaTest}.
     * But it passes when the test is annotated with {@code @SpringBootTest(webEnvironment = NONE)}
     */
    def "LastModifiedDate value is updated"() {
        given:
            def persisted1 = repo.save(new OneToManyEntity())
            sleep(1000)
            persisted1.setChildren([])
            def persisted2 = repo.save(persisted1)
        expect:
            persisted2.lastModifiedDate.isAfter(persisted1.lastModifiedDate)
    }
}
