package com.jpa.board.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jpa.board.dto.ImgMapping;
import com.jpa.board.dto.ItemDTO;
import com.jpa.board.dto.ItemImgDTO;
import com.jpa.board.entity.ItemEntity;
import com.jpa.board.entity.ItemImgEntity;
import com.jpa.board.entity.repository.ItemImgRepository;
import com.jpa.board.service.ItemImgService;
import com.jpa.board.service.ItemService;
import com.jpa.board.util.PageUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Controller
public class ItemController {

    private final ItemService itemService;
    private final ItemImgService itemImgService;

    // 상품 페이지 보기
    @GetMapping(value="/item/itemPage")
    public String getItemPage(Model model) {
        model.addAttribute("itemDTO", new ItemDTO());

        return "/item/itemPage";
        
    }

    // 상품 등록 페이지
    @PostMapping(value="/item/itemPage")
    public String postItemPage(@Valid ItemDTO itemDTO, BindingResult bindingResult, Model model, 
        @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        
        // 상품 등록 시 필수 값이 없다면 다시 상품 등록 페이지로 전환한다.
        if(bindingResult.hasErrors()) { 
            return "/item/itemPage";
        }
        
        // 상품 등록 시 첫 번째 이미지가 없다면 에러 메세지와 함께 상품 등록 페이지로 전환합니다.
        // 상품의 첫 번째 이미지는 메인 페이지에서 보여줄 상품 이미지로 사용하기 위해서 필수 값으로 지정한다.
        if(itemImgFileList.get(0).isEmpty() && itemDTO.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "/item/itemPage";

        }

        try {
            // 상품 저장 로직을 호출. 매개 변수로 상품 정보와 상품 이미지 정보를 담고 있는 itemImgFileList를 넘겨준다.
            itemService.saveItem(itemDTO, itemImgFileList);

        }catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러 발생" + e);
            return "/item/itemPage";

        }
        
        // 상품이 정상적으로 등록되었으면 메인 페이지로 이동
        return "redirect:/";

    }

    // 상품 페이지 수정
    @GetMapping(value = "/item/itemPage/{itemId}")
    public String itemDTL(@PathVariable("itemId") Long itemId, Model model) {

        try {
            // 조회한 상품 데이터를 모델에 담아서 뷰로 전달
            ItemDTO itemDTO = itemService.getItemDTL(itemId);
            model.addAttribute("itemDTO", itemDTO);

        // 상품 엔티티가 존재하지 않을 경우 에러메세지를 담아서 상품 등록 페이지로 이동
        }catch(EntityNotFoundException e) { 
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("itemDTO", new ItemDTO());
            return "admin/itemPage";

        }
        return "admin/item/itemPage";

    }

    // 상품 페이지 수정
    @PostMapping(value = "/item/itemPage/{itemId}")
    public String itemUpdate(@Valid ItemDTO itemDTO, BindingResult bindingResult,
        @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {

        if(bindingResult.hasErrors()) {
            return "admin/item/item/itemPage";

        }

        if(itemImgFileList.get(0).isEmpty() && itemDTO.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "admin/item/itemPage";

        }

        try {
            // 상품 수정 로직을 호출
            itemService.updateItem(itemDTO, itemImgFileList);

        }catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "admin/item/itemPage";

        }
        return "redirect:/admin/adminHomepage";

    }

    // 상품 view 페이지
    @GetMapping(value = "/item/itemViewPage")
    public String getItemViewPage(Model model,
        @RequestParam(name = "page", defaultValue = "1") int pageNum, 
        @RequestParam(name="searchType", defaultValue="itemNm", required=false) String searchType, 
        @RequestParam(name="keyword", defaultValue="", required=false) String keyword, ItemImgDTO itemImgDTO) throws Exception { 

        int listCount = 5; //페이지 리스트에 보여질 페이지 갯수
        int postNum = 5; //한 페이지에 보여질 게시물 행의 갯수

        Page<ItemEntity> list = itemService.list(searchType, keyword, pageNum, postNum);
        
        PageUtil pageUtil = new PageUtil();

        int totalCount = (int)list.getTotalElements();

        // List<ImgMapping> imgnm = itemImgService.getImgName();

        // log.info("imn :" + imgnm);

        // log.info("nm = "+ nm);

        // log.info("아이템뷰페이지");

        // model.addAttribute("imgName", itemImgService.getImgName());
        model.addAttribute("list", itemService.list(searchType, keyword, pageNum, postNum));
		model.addAttribute("page", pageNum);
		model.addAttribute("searchType", searchType);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageListView", pageUtil.getPageList(pageNum, postNum, listCount, totalCount, searchType, keyword));

        return "/item/itemViewPage";

    }

    
    @GetMapping(value = "/item/itemMainPage")
    public String getMainPage(Model model, @RequestParam(name = "page", defaultValue = "1") int pageNum, 
        @RequestParam(name="searchType", defaultValue="mtitle", required=false) String searchType, 
        @RequestParam(name="keyword", defaultValue="", required=false) String keyword) throws Exception {

        int listCount = 5; //페이지 리스트에 보여질 페이지 갯수
        int postNum = 5; //한 페이지에 보여질 게시물 행의 갯수

        Page<ItemEntity> list = itemService.list(searchType, keyword, pageNum, postNum);

        PageUtil pageUtil = new PageUtil();

        int totalCount = (int)list.getTotalElements();

        model.addAttribute("list", itemService.list(searchType, keyword, pageNum, postNum));
        model.addAttribute("page", pageNum);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageListView", pageUtil.getPageList(pageNum, postNum, listCount, totalCount, searchType, keyword));

        return "/item/itemMainPage";
        
    }

}