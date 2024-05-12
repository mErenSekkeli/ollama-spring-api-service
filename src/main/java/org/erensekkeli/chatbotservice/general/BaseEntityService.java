package org.erensekkeli.chatbotservice.general;

import lombok.Getter;
import org.erensekkeli.chatbotservice.exceptions.ItemNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
public abstract class BaseEntityService<E extends BaseEntity, R extends JpaRepository<E, Long>>{

    private final R repository;

    protected BaseEntityService(R repository) {
        this.repository = repository;
    }

    public E save(E entity) {
        BaseAdditionalFields baseAdditionalFields = entity.getBaseAdditionalFields();

        if (baseAdditionalFields == null) {
            baseAdditionalFields = new BaseAdditionalFields();
        }

        if (entity.getId() == null) {
            baseAdditionalFields.setCreateDate(LocalDateTime.now());
        }

        baseAdditionalFields.setUpdateDate(LocalDateTime.now());

        entity.setBaseAdditionalFields(baseAdditionalFields);

        entity = repository.save(entity);

        return entity;
    }

    public List<E> findAll() {
        return repository.findAll();
    }

    public Optional<E> findById(Long id){
        return repository.findById(id);
    }

    public E findByIdWithControl(Long id) {
        return repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Data Has Not Found!"));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public void delete(E entity){
        repository.delete(entity);
    }

    public boolean existById(Long id){
        return repository.existsById(id);
    }
}
