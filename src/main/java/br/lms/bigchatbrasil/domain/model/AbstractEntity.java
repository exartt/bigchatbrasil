package br.lms.bigchatbrasil.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.data.annotation.Version;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name="entity_uid", unique=true, nullable=false, updatable=false, length=36)
    private String uid;

    @Version
    private Integer version;

    public Integer getVersion() {
        if(version == null){
            this.version = 0;
        }
        return version;
    }

    public AbstractEntity(){
        if (this.uid == null){
            uid();
        }
    }

    @Override
    public boolean equals(Object o) {
        return (o == this || (o instanceof AbstractEntity && uid().equals(((AbstractEntity)o).uid())));
    }

    @Override
    public int hashCode() {
        return uid().hashCode();
    }

    public static class AbstractEntityListener {
        @PrePersist
        @PreUpdate
        public void onPrePersist(AbstractEntity abstractEntity) {
            abstractEntity.uid();
        }
    }

    private String uid() {
        if (uid == null)
            uid = UUID.randomUUID().toString();
        return uid;
    }
}