package com.springboottrain.app.controller;

import java.io.PrintWriter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboottrain.app.domain.Board;
import com.springboottrain.app.service.BoardService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BoardController {
		
		@Autowired
		private BoardService boardService;
		
		@GetMapping({"/", "/boardList"})
		public String boardList(Model model,
				@RequestParam(value="pageNum", required=false,
				defaultValue="1") int pageNum,
				@RequestParam(value="type", required=false,
				defaultValue="null") String type,
				@RequestParam(value="keyword", required=false,
				defaultValue="null") String keyword) {
			Map<String, Object> modelMap = boardService.boardList(pageNum, type, keyword);
			model.addAllAttributes(modelMap);
			return "views/boardList";
		}
		
		
		@GetMapping("/addBoard")
		public String addBoard(){
			return "views/writeForm";
		}
		
		@PostMapping("/addBoard")
		public String addBoard(Board board) {
			log.info("title :", board.getTitle());
			boardService.addBoard(board);
			return "redirect:boardList";
		}
		
		@PostMapping("/updateForm")
		public String updateBoard(Model model,
				HttpServletResponse response, PrintWriter out,
				@RequestParam("no") int no, @RequestParam("pass") String pass,
				@RequestParam(value="pageNum", defaultValue="1")int pageNum,
				@RequestParam(value="type", defaultValue="null") String type,
				@RequestParam(value="keyword", defaultValue="null")String keyword) {
			boolean isPassCheck = boardService.isPassCheck(no, pass);
			if(! isPassCheck) {
				response.setContentType("text/html; charset=utf-8");
				out.println("<script>");
				out.println("alert('비밀번호가 맞지 않습니다.');");
				out.println("history.back();");
				out.println("</script>");
				return null;
			}
			Board board = boardService.getBoard(no, false);
			boolean searchOption = (type.equals("null")
			|| keyword.equals("null")) ? false : true;
			
			model.addAttribute("board", board);
			model.addAttribute("pageNum", pageNum);
			model.addAttribute("searchOption", searchOption);
			
			if(searchOption) {
				model.addAttribute("type", type);
				model.addAttribute("keyword", keyword);
			}
			return "views/updateForm";
		}
		
		@PostMapping("/update")
		public String updateBoard(Board board, RedirectAttributes reAttrs,
				HttpServletResponse response, PrintWriter out,
				@RequestParam("no") int no, @RequestParam("pass") String pass,
				@RequestParam(value="pageNum", defaultValue="1") int pageNum,
				@RequestParam(value="type", defaultValue="null") String type,
				@RequestParam(value="keyword", defaultValue="null") String keyword) {
			
			
			boolean isPassCheck = boardService.isPassCheck(board.getNo(), board.getPass());
			if(! isPassCheck){
				response.setContentType("text/html; charset=utf-8");
				out.println("<script>");
				out.println("alert('비밀번호가 맞지 않습니다.');");
				out.println("history.back();");
				out.println("</script>");				
				return null;
			}
			boardService.updateBoard(board);
			boolean searchOption = (type.equals("null")
					|| keyword.equals("null")) ? false : true;
			
			reAttrs.addAttribute("searchOption", searchOption);
			reAttrs.addAttribute("pageNum", pageNum);
			
			if(searchOption) {
				reAttrs.addAttribute("type", type);
				reAttrs.addAttribute("keyowrd", keyword);
			}
			return "redirect:boardList";
		}
		
		
		
		@PostMapping("/delete")
		public String deleteBoard(RedirectAttributes reAttrs,
				HttpServletResponse response, PrintWriter out,
				@RequestParam("no") int no,
				@RequestParam("pass") String pass,
				@RequestParam(value="pageNum", defaultValue="1") int pageNum,
				@RequestParam(value="type", defaultValue="null") String type,
				@RequestParam(value="keyword", defaultValue="null") String keyword) {
			
			boolean isPassCheck = boardService.isPassCheck(no, pass);
			if(! isPassCheck) {
				response.setContentType("text/html; charset=utf-8");
				out.println("<script>");
				out.println("alert('비밀번호가 맞지 않습니다.');");
				out.println("history.back();");
				out.println("</script>");
				
				return null;
			}
			boardService.deleteBoard(no);
			
			boolean searchOption = (type.equals("null")
					|| keyword.equals("null")) ? false : true;
			
			reAttrs.addAttribute("pageNum", pageNum);
			reAttrs.addAttribute("searchOption", searchOption);
			
			if(searchOption) {
				reAttrs.addAttribute("type", type);
				reAttrs.addAttribute("keyword", keyword);
			}
			return "redirect:boardList";
		}
		
		@GetMapping("/boardDetail")
		public String getBoard(Model model, @RequestParam("no") int no,
				@RequestParam(value="pageNum", defaultValue="1") int pageNum,
				@RequestParam(value="type", defaultValue="null") String type,
				@RequestParam(value="keyword", defaultValue="null") String keyword) {
			
			boolean searchOption = (type.equals("null")
					|| keyword.equals("null")) ? false : true;
			
			Board board = boardService.getBoard(no, true);
			model.addAttribute("board", board);
			model.addAttribute("pageNum", pageNum);
			model.addAttribute("searchOption", searchOption);
			
			if(searchOption) {
				model.addAttribute("type", type);
				model.addAttribute("keyword", keyword);
			}
			return "views/boardDetail";
			
		}
		




}

