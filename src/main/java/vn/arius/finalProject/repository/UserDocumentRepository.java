package vn.arius.finalProject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import vn.arius.finalProject.entity.elasticsearch.UserDocument;

public interface UserDocumentRepository extends ElasticsearchRepository<UserDocument, String> {
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"name^3\", \"email\", \"address\", \"role.name\"], \"fuzziness\": \"AUTO\"}}")
    Page<UserDocument> searchByQuery(String query, Pageable pageable);
}
