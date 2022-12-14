package com.jpa.board.dto;

import org.springframework.beans.factory.annotation.Value;

public interface ImgMapping {

    @Value("#{target.imgName}")
    String getImgName();

}
