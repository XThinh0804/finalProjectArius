package vn.arius.finalProject.controller;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.arius.finalProject.dto.response.ResProductDTO;
import vn.arius.finalProject.dto.response.ResTagDTO;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;
import vn.arius.finalProject.entity.Product;
import vn.arius.finalProject.entity.Tag;
import vn.arius.finalProject.entity.User;
import vn.arius.finalProject.service.TagService;
import vn.arius.finalProject.util.error.InvalidException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping("/tags")
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) throws InvalidException {
        boolean isNameExist = tagService.isNameExist(tag.getName());
        if (isNameExist) {
            throw new InvalidException("Tag name đã tồn tại.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.create(tag));
    }

    @DeleteMapping("/tags")
    public ResponseEntity<Void> deleteAllTag(@RequestBody List<Long> ids) {
        this.tagService.deleteAll(ids);
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("/tags")
    public ResponseEntity<Tag> updateTag(@RequestBody Tag tag) throws InvalidException {
        Tag currentTag = tagService.fetchById(tag.getId());
        if (currentTag == null) {
            throw new InvalidException("Tag không tồn tại.");
        }
        return ResponseEntity.ok().body(tagService.update(tag));

    }

    @DeleteMapping("/tags/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable("id") long id) throws InvalidException {
        Tag currentTag = tagService.fetchById(id);
        if (currentTag == null) {
            throw new InvalidException("Tag không tồn tại.");
        }
        tagService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/tags/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable("id") long id) throws InvalidException {
        Tag currentTag = tagService.fetchById(id);
        if (currentTag == null) {
            throw new InvalidException("Tag không tồn tại.");
        }
        return ResponseEntity.ok().body(currentTag);
    }

    @GetMapping("/tags")
    public ResponseEntity<ResultPaginationDTO> getAllTags(@Filter Specification<Tag> spec, Pageable pageable) {
        return ResponseEntity.ok().body(tagService.fetchAllTags(spec, pageable));
    }

    @GetMapping("/tagsdto")
    public ResponseEntity<List<ResTagDTO>> getAllTagsDTO() {
        return ResponseEntity.ok().body(this.tagService.getTagsDTO());
    }

    @GetMapping("/tagsdto/{id}")
    public ResponseEntity<ResTagDTO> getTagsDTOById(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(this.tagService.getTagDTOById(id));
    }
}
