package com.jpa.board.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jpa.board.dto.ItemDTO;
import com.jpa.board.dto.ItemImgDTO;
import com.jpa.board.entity.ItemEntity;
import com.jpa.board.entity.ItemImgEntity;
import com.jpa.board.entity.repository.ItemImgRepository;
import com.jpa.board.entity.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
    
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    // 상품 검색
    public Page<ItemEntity> list(String searchType, String keyword, int pageNum, int postNum) {

        PageRequest pageRequest = PageRequest.of(pageNum-1,postNum,Sort.by(Direction.DESC, "id"));

        if(searchType.equals("itemNm")) {
            return itemRepository.findByItemNmContaining(keyword, pageRequest);

        }else {
            return itemRepository.findAll(pageRequest);

        }

    }

    public Long saveItem(ItemDTO itemDTO, List<MultipartFile> itemImgFileList) throws Exception {

        // 상품 등록
        ItemEntity itemEntity = itemDTO.createItem(); // 상품 등록 폼으로부터 입력 받은 데이터를 이용하여 item 객체를 생성한다.
        itemRepository.save(itemEntity); // 상품 데이터를 저장한다.

        // 이미지 등록
        for(int i = 0 ; i < itemImgFileList.size() ; i++) {
            ItemImgEntity itemImgEntity = new ItemImgEntity();
            itemImgEntity.setItemeEntity(itemEntity);

            if(i == 0) { // 첫 번째 이미지일 경우 대표 상품 이미지 여부 값을 "Y"로 세팅한다, 나머지 상품 이미지는 "N"으로 설정한다.
                itemImgEntity.setRepimgYn("Y");

            }else {
                itemImgEntity.setRepimgYn("N");

            }
            itemImgService.saveItemImg(itemImgEntity, itemImgFileList.get(i)); // 상품의 이미지 정보를 저장한다.

        }
        return itemEntity.getId();

    }
    
    // 상품 데이터를 읽어오는 트랜잭션을 읽기 전용으로 설정
    @Transactional(readOnly = true) // 이럴 경우 JPA가 더티체킹(변경감지)을 수행하지 않아서 성능을 향상 시킬 수 있다.
    public ItemDTO getItemDTL(Long itemId) {

        // 해당 상품의 이미지를 조회한다. 등록순으로 가져오기 위해서 상품 이미지 아이디 오름차순으로 가지고 온다.
        List<ItemImgEntity> itemImgList = itemImgRepository.findByIdOrderByIdAsc(itemId);

        List<ItemImgDTO> itemImgDTOList = new ArrayList<>();

        // 조회한 ItemImgEntity를 ItemImgDTO 개게로 만들어서 리스트에 추가
        for(ItemImgEntity itemImgEntity : itemImgList) {
            ItemImgDTO itemImgDTO = ItemImgDTO.of(itemImgEntity);
            itemImgDTOList.add(itemImgDTO);

        }

        // 상품의 아이디를 통해 상품 엔티티를 조회한다. 존재하지 않을 때는 EntityNotFoundException을 발생
        ItemEntity itemEntity = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        ItemDTO itemDTO = ItemDTO.of(itemEntity);
        itemDTO.setItemImgDtoList(itemImgDTOList);

        return itemDTO;

    }

    public Long updateItem(ItemDTO itemDTO, List<MultipartFile> itemImgFileList) throws Exception {

        // 상품 수정
        // 상품 등록 화면으로부터 전달 받은 상품 아이디를 이용하여 상품 엔티티를 조회
        ItemEntity itemEntity = itemRepository.findById(itemDTO.getId()).orElseThrow(EntityNotFoundException::new);

        // 상품 등록 화면으로부터 전달 받은 ItemDTO를 통해 상품 엔티티를 업데이트
        itemEntity.updateItem(itemDTO);

        // 상품 이미지 아이디 리스트를 조회
        List<Long> itemImgIds = itemDTO.getItemImgIds();

        // 이미지 등록
        for(int i = 0 ; i < itemImgFileList.size() ; i++) {
            // 상품 이미지를 업데이트하기 위해서 updateItemImg() 메소드에 상품 이미지 아이디와,
            // 상품 이미지 파일 경로를 파라미터로 전달
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));

        }
        return itemEntity.getId();

    }
}
