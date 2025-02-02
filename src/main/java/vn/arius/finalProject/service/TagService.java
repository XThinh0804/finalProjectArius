package vn.arius.finalProject.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vn.arius.finalProject.dto.response.ResTagDTO;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;
import vn.arius.finalProject.entity.Tag;

import java.util.List;

public interface TagService {
    boolean isNameExist(String name);

    Tag create(Tag tag);

    Tag update(Tag tag);

    void delete(long id);

    Tag fetchById(long id);

    ResultPaginationDTO fetchAllTags(Specification<Tag> spec, Pageable pageable);

    void deleteAll(List<Long> ids);

    List<ResTagDTO> getTagsDTO();

    ResTagDTO getTagDTOById(long id);
}
