package vn.arius.finalProject.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.arius.finalProject.dto.response.ResProductDTO;
import vn.arius.finalProject.dto.response.ResTagDTO;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;
import vn.arius.finalProject.entity.Product;
import vn.arius.finalProject.entity.Tag;
import vn.arius.finalProject.repository.TagRepository;
import vn.arius.finalProject.service.TagService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public boolean isNameExist(String name) {
        return this.tagRepository.existsByName(name);
    }

    @Override
    public Tag create(Tag tag) {
        return this.tagRepository.save(tag);
    }

    @Override
    public Tag update(Tag tag) {
        Optional<Tag> currentTag = this.tagRepository.findById(tag.getId());
        if (currentTag.isPresent()) {
            currentTag.get().setName(tag.getName());
            currentTag.get().setDescription(tag.getDescription());
            return this.tagRepository.save(currentTag.get());
        }
        return null;
    }

    @Override
    public void delete(long id) {
        this.tagRepository.deleteById(id);
    }

    @Override
    public Tag fetchById(long id) {
        Optional<Tag> currentTag = this.tagRepository.findById(id);
        if (currentTag.isPresent()) {
            return currentTag.get();
        } else {
            return null;
        }
    }

    @Override
    public ResultPaginationDTO fetchAllTags(Specification<Tag> spec, Pageable pageable) {
        Page<Tag> pageTag = this.tagRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageTag.getTotalPages());
        mt.setTotal(pageTag.getTotalElements());

        rs.setMeta(mt);

        rs.setResult(pageTag.getContent());

        return rs;
    }

    @Override
    public void deleteAll(List<Long> ids) {
        this.tagRepository.deleteAllById(ids);
    }

    @Override
    public List<ResTagDTO> getTagsDTO() {
        List<Tag> tags = this.tagRepository.findAll();
        List<ResTagDTO> listResTagDTO = tags.stream().map(item -> this.convertResTagDTO(item)).collect(Collectors.toList());
        return listResTagDTO;
    }

    @Override
    public ResTagDTO getTagDTOById(long id) {
        Tag tag = this.tagRepository.findById(id).get();
        ResTagDTO resTagDTO = this.convertResTagDTO(tag);
        return resTagDTO;
    }

    public ResTagDTO convertResTagDTO(Tag tag) {
        ResTagDTO resTagDTO = new ResTagDTO();
        resTagDTO.setId(tag.getId());
        resTagDTO.setName(tag.getName());
        List<ResProductDTO> listResProductDTO = tag.getProducts().stream().map(item -> this.convertResProductDTO(item)).collect(Collectors.toList());
        resTagDTO.setProductsDTO(listResProductDTO);
        return resTagDTO;
    }

    public ResProductDTO convertResProductDTO(Product product) {
        ResProductDTO resProductDTO = new ResProductDTO();
        resProductDTO.setId(product.getId());
        resProductDTO.setName(product.getName());
        resProductDTO.setImage(product.getImage());
        resProductDTO.setPrice(product.getPrice());
        return resProductDTO;
    }
}
