package br.com.f1rst.datas.user.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "tb_roles")
public class RoleEntity  implements Serializable {

    @Id
    @Column(name = "id",  unique = true, nullable = false)
    private String id;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @ElementCollection
    @CollectionTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "permission")
    private List<String> permissions;

    @Column(name = "nm_created", nullable = false)
    private String nmCreated;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_created", nullable = false, updatable = false)
    private Date dtCreated;

    @Column(name = "nm_edited")
    private String nmEdited;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_updated", insertable = false)
    private Date dtUpdated;
}

