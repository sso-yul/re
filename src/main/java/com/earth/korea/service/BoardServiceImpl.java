package com.earth.korea.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.earth.korea.dao.BoardDao;
import com.earth.korea.domain.BoardDTO;
import com.earth.korea.domain.SearchItem;

@Service
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	BoardDao boardDao;

	@Override
	public List<BoardDTO> getPage(Map map) throws Exception {
		
		return boardDao.selectPage(map);
	}

	@Override
	public int getCount() throws Exception {
		
		return boardDao.count();
	}

	@Override
	public BoardDTO read(Integer bno) throws Exception {
		BoardDTO boardDTO = boardDao.select(bno);
		//비즈니스 로직추가(조회수 증가)
		boardDao.increaseViewCnt(bno);		
		return boardDTO;
	}

	@Override
	public int remove(Integer bno, String writer) throws Exception {
		
		return boardDao.delete(bno, writer);
	}

	@Override
	public int write(BoardDTO boardDTO) throws Exception {
		
		return boardDao.insert(boardDTO);
	}

	@Override
	public int modify(BoardDTO boardDTO) throws Exception {
		
		return boardDao.update(boardDTO);
	}

	@Override
	public int getSearchResultCnt(SearchItem sc) throws Exception {
		
		return boardDao.searchResultCnt(sc);
	}

	@Override
	public List<BoardDTO> getSearchSelectPage(SearchItem sc) throws Exception {
		
		return boardDao.searchSelectPage(sc);
	}

}
