package jpabook.jpashop.file;

import jpabook.jpashop.domain.UploadFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileStore {

    //파일 저장 위치
    private String fileDir = "C:/Users/ghksg/file/";

    public String getFullPath(String filename){
        return fileDir + filename;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()){
            return null;
        }

        //사용자가 업로드한 파일네임을 가져온다.
        String originalFilename = multipartFile.getOriginalFilename();

        //uuid로 서버에 저장하는 파일명을 만들고 사용자가 업로드한 파일네임의 확장자를 붙혀준다
        String storeFileName = createStoreFileName(originalFilename);

        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return new UploadFile(originalFilename, storeFileName);
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }


}
