package spring.jpa;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Entity
@Table(name = "many2one")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
class ManyToOneEntity extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    @EqualsAndHashCode.Include
    Integer id;

    @ManyToOne(optional = false, fetch = LAZY)
    OneToManyEntity parent;
}
