package com.earth.korea.service;

import java.util.List;
import java.util.Map;

import com.earth.korea.domain.BoardDTO;
import com.earth.korea.domain.SearchItem;

public interface BoardService {
	
	List<BoardDTO> getPage(Map map) throws Exception;	
	int getCount() throws Exception;	
	BoardDTO read(Integer bno) throws Exception;	
	int remove(Integer bno, String writer) throws Exception;	
	int write(BoardDTO boardDTO) throws Exception;	
	int modify(BoardDTO boardDTO) throws Exception;
	int getSearchResultCnt(SearchItem sc) throws Exception;
	List<BoardDTO> getSearchSelectPage(SearchItem sc) throws Exception;
}
