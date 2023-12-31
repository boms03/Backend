package com.example.simpleboard.post.service;

import com.example.simpleboard.board.db.BoardEntity;
import com.example.simpleboard.board.db.BoardRepository;
import com.example.simpleboard.common.Api;
import com.example.simpleboard.common.Pagination;
import com.example.simpleboard.post.db.PostEntity;
import com.example.simpleboard.post.db.PostRepository;
import com.example.simpleboard.post.model.PostRequest;
import com.example.simpleboard.post.model.ViewRequest;
import com.example.simpleboard.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
//    private final ReplyService replyService;
    private final BoardRepository boardRepository;


    public PostEntity save(
            PostRequest postRequest
    ){
        var boardEntity = boardRepository.findById(postRequest.getBoardId()).get();
        var request = PostEntity.builder()
                .board(boardEntity)//대기
                .userName(postRequest.getUserName())
                .password(postRequest.getPassword())
                .email(postRequest.getEmail())
                .content(postRequest.getContent())
                .title(postRequest.getTitle())
                .postedAt(LocalDateTime.now())
                .status("REGISTERED")
                .build();
        return postRepository.save(request);
    }

    public PostEntity view(
            ViewRequest viewRequest
    ){
        return postRepository.findFirstByIdAndStatusOrderByIdDesc(viewRequest.getPostId(),"REGISTERED") // findById returns Optional
                .map(it -> { // if data exist
                    if(!it.getPassword().equals(viewRequest.getPassword())){
                        String format = "Password does not match %s vs %s";
                        throw new RuntimeException(String.format(format,viewRequest.getPassword(),it.getPassword()));
                    }

//                    var replyList = replyService.findAllByPostId(viewRequest.getPostId());
//                    it.setReplyEntityList(replyList);
                    return it;
                }).orElseThrow( // if null throw
                        ()-> new RuntimeException("The post does not exist : " + viewRequest.getPostId())
                );
    }

    public Api<List<PostEntity>> viewAll(Pageable pageable){
        var list = postRepository.findAll(pageable);

        var pagination = Pagination.builder()
                .page(list.getNumber())
                .size(list.getSize())
                .currentElements(list.getNumberOfElements())
                .totalElements(list.getTotalElements())
                .totalPage(list.getTotalPages())
                .build();
        var response = Api.<List<PostEntity>>builder()
                .body(list.toList())
                .pagination(pagination)
                .build();

        return response;
    }

    public PostEntity delete(
            ViewRequest viewRequest
    ){
        return postRepository.findById(viewRequest.getPostId()) // findById returns Optional
                .map(it -> { // if data exist
                    if(!it.getPassword().equals(viewRequest.getPassword())){
                        String format = "Password does not match %s vs %s";
                        throw new RuntimeException(String.format(format,viewRequest.getPassword(),it.getPassword()));
                    }
                    it.setStatus("UNREGISTERED");
                    postRepository.save(it);
                    return it;
                }).orElseThrow( // if null throw
                        ()-> new RuntimeException("The post does not exist " + viewRequest.getPostId())
                );

    }
}
