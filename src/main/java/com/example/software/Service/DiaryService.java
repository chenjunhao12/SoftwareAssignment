package com.example.software.Service;

import com.example.software.Entity.DiaryDetail.Cover;
import com.example.software.Entity.DiaryDetail.PaperColor;
import com.example.software.Entity.DiaryDetail.TypeOfPaper;

import java.util.List;

public interface DiaryService {

    List<Cover> getCovers();

    List<PaperColor> getPaperColors();

    List<TypeOfPaper> getTypeOfPapers();

    boolean addCover(Cover cover);
}
