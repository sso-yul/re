package com.earth.korea.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.earth.korea.domain.BoardDTO;
import com.earth.korea.domain.PageResolver;
import com.earth.korea.domain.SearchItem;
import com.earth.korea.domain.User;
import com.earth.korea.service.BoardService;


@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	BoardService boardService;
	
	@GetMapping("/list")
	public String list(SearchItem sc,
						Model m,
						HttpServletRequest request) {
		if(!loginCheck(request))
			return "redirect:/login/login?toURL=" +request.getRequestURL();
		
//		if(page == null) page=1;
//		if(pageSize==null) pageSize=10;
		
		try {
			
			int totalCnt = boardService.getSearchResultCnt(sc);
			m.addAttribute("totalCnt", totalCnt);
			
			PageResolver pageResolver = new PageResolver(totalCnt, sc);
			
			List<BoardDTO> list = boardService.getSearchSelectPage(sc);
			m.addAttribute("list", list);
			m.addAttribute("pr", pageResolver);
			
		} catch (Exception e) { e.printStackTrace(); }
		
		return "boardList";			// 로그인 한 상태, 게시물 화면 목록으로 이동
		
	}
	
	@GetMapping("/read")
	public String read(Integer bno, SearchItem sc, Model m) {
		try {
			BoardDTO boardDTO = boardService.read(bno);
			//m.addAttribute("boardDTO", boardDTO);
			
			// BoardDTO 타입의 첫문자를 소문자로 변환해서 key명
			m.addAttribute(boardDTO);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/board/list";
		}
		
		return "board";		
	}
	
	@PostMapping("/remove")
	public String remove(Integer bno, Integer page, Integer pageSize,
						RedirectAttributes rattr, HttpSession session) {
		String writer = (String) session.getAttribute("id");
		String msg = "DEL_OK";
		
		try {
			if (boardService.remove(bno, writer) != 1)
				throw new Exception("Delete failed.");
		} catch (Exception e) {			
			e.printStackTrace();
			msg = "DEL_ERR";
		}
		
		//삭제 후 메시지가 한 번만 출력돼야 함
		//Model이 아닌 RedirectAttributes에 저정하면 한 번만 나옴
		//addFlashAttribute() : 한 번 저장하고 사라짐. 세션에 잠깐 저장했다가 지워버림
		rattr.addAttribute("page", page);
		rattr.addAttribute("pageSize", pageSize);
		rattr.addFlashAttribute("msg", msg);
		
		return "redirect:/board/list";
	}
	
	@GetMapping("/write")
	public String write(Model m) {
		m.addAttribute("mode", "new");
		
		return "board";		// board.jsp 읽기와 쓰기에 사용, 글쓰기에 사용할 때는 mode=new
	}
	
	@PostMapping("/write")
	public String write(BoardDTO boardDTO, RedirectAttributes rattr,
						Model m, HttpSession session) {
		String writer = (String) session.getAttribute("id");
		boardDTO.setWriter(writer);
		
		try {
			if(boardService.write(boardDTO) != 1) {
				throw new Exception("Write failed");				
			}
			rattr.addFlashAttribute("msg","WRT_OK");
			return "redirect:/board/list";
			
		} catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("mode", "new");				//글쓰기 모드
			m.addAttribute("boardDTO", boardDTO);		//등록하려던 내용을 저장
			m.addAttribute("msg", "WRT_ERR");
			return "board";
		}

	}
	
	@PostMapping("/modify")
	public String modify(BoardDTO boardDTO, Integer page, Integer pageSize,
						RedirectAttributes rattr, Model m, HttpSession session) {
		String writer = (String) session.getAttribute("id");
		boardDTO.setWriter(writer);
		
		try {
			if(boardService.modify(boardDTO) != 1)
				throw new Exception("Modify failed");
			
			rattr.addFlashAttribute("msg","MOD_OK");
			return "redirect:/board/list";
		} catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("boardDTO", boardDTO);
			m.addAttribute("page", page);
			m.addAttribute("pageSize", pageSize);
			m.addAttribute("msg","MOD_ERR");
			return "board";			//수정등록하려던 내용을 보여줌
		}
		
	}

	private boolean loginCheck(HttpServletRequest request) {
		// 1. 세션을 얻어 (false는 session이 없어도 새로 생성하지 않음, 반환값 null)
		HttpSession session = request.getSession(false);
		// 2. 세션에 id가 있는지 확인, 있으면 true 반환
		return session != null && session.getAttribute("id") != null;
	}

}
