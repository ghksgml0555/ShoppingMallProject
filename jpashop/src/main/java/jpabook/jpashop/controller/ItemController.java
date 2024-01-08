package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.UploadFile;
import jpabook.jpashop.file.FileStore;
import jpabook.jpashop.service.ItemService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(@ModelAttribute BookForm form) throws IOException {

        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());

        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        book.setUploadFileName(attachFile.getUploadFileName());
        book.setStoreFileName(attachFile.getStoreFileName());

        itemService.saveItem(book);
        return "redirect:/items";
    }

    //<img>태그로 이미지 조회할때 이미지 바이너리를 반환
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:"+fileStore.getFullPath(filename));
        //이 경로에있는 파일에 접근해서 그 파일을 스트림으로 반환한다.
    }


    @GetMapping("/items")
    public String list(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) throws IOException {
        Book item = (Book) itemService.findOne(itemId);

        BookForm newForm = new BookForm();
        newForm.setId(item.getId());
        newForm.setName(item.getName());
        newForm.setPrice(item.getPrice());
        newForm.setStockQuantity(item.getStockQuantity());
        newForm.setAuthor(item.getAuthor());
        newForm.setIsbn(item.getIsbn());


        model.addAttribute("form", newForm);
        return "items/updateItemForm";
    }


    /**
     * 상품 수정, 권장 코드
     */
    @PostMapping(value = "/items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) throws IOException {
        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity(),
                attachFile.getUploadFileName(), attachFile.getStoreFileName());
        return "redirect:/items";
    }

    @GetMapping("items/{itemId}/delete")
    public String deleteItem(@PathVariable Long itemId){
        itemService.deleteItem(itemId);
        return "redirect:/items";
    }
}
