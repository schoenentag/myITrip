package co.itrip.prj.consult.web;



import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import co.itrip.prj.cmmncd.service.CmmnCdService;
import co.itrip.prj.consult.service.ConsultChatVO;
import co.itrip.prj.consult.service.ConsultDtVO;
import co.itrip.prj.consult.service.ConsultService;
import co.itrip.prj.consult.service.ConsultVO;
import co.itrip.prj.guide.service.GuideService;
import co.itrip.prj.guide.service.GuideVO;
import co.itrip.prj.payform.service.PayformService;
import co.itrip.prj.payform.service.PayformVO;

@Controller
public class ConsultController {

	@Autowired
	private GuideService guService; // 가이드 서비스
	
	@Autowired
	private CmmnCdService cdService; // 공통코드 서비스
	
	@Autowired
	private ConsultService conService; // 1:1상담 서비스
	
	@Autowired
	private PayformService payService; // 결제 서비스
	
	
	// 상담메인페이지 페이징
	@GetMapping("/consultList.do")
	public String findPage(ConsultVO vo, Model model, HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		vo.setEnnc("활성화");
		model.addAttribute("pageInfo", PageInfo.of(conService.findAll(vo)));
		model.addAttribute("job", cdService.jobCdList());
		return "consult/consultList";
	}
	
	// 카테고리별 검색 - 상담메인페이지
	@RequestMapping("/ajaxJobListSearch.do")
	@ResponseBody
	public PageInfo<ConsultVO> ajaxJobListSearch(ConsultVO vo, Model model, HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "1") int pageNum,
			@RequestParam(required = false, defaultValue = "8") int pageSize){
		PageHelper.startPage(pageNum, pageSize);
		vo.setEnnc("활성화");
		return PageInfo.of(conService.findAll(vo));
	}
	
	// 상담 단건 조회 (더보기)
	@RequestMapping("/consultSelectOne.do")
	public String consultSelectOne(ConsultVO vo, Model model, ConsultDtVO dtvo) {
		model.addAttribute("consultOne", conService.consultSelectOne(vo));// 상담 정보
		model.addAttribute("consultDt", conService.consultDtList(dtvo));  // 상담 가능 시간
		return "consult/consultSelectOne";
	}
		
	
	// 상담 등록 페이지로 이동
	@RequestMapping("/consultStart.do")
	public String consultStart(Model model, GuideVO vo, ConsultVO cvo, Principal principal) {
		vo.setGuideId(principal.getName());
		model.addAttribute("guide", guService.guideSelect(vo)); // 로그인된 가이드 아이디 확인 (단건조회)
		model.addAttribute("joblist", cdService.jobCdList()); // 카테고리 공통코드
		return "consult/consultStart";
	}
	
	// 상담 등록 처리
	@PostMapping("/consultInsert.do")
	public String consultInsert(ConsultVO vo) {
		conService.consultInsert(vo);
		// 가이드 메인페이지로 이동
		return "redirect:gconsult.do";
	}
	
	// 신청된 상담 리스트
	@GetMapping("/alreadyConsult.do")
	public String alreadyConsult(Principal principal, Model model, ConsultVO vo) {
		 vo.setGuideId(principal.getName());
		 model.addAttribute("alreadyList", conService.alreadyConsult(vo));
		return "guide/alreadyConsult";
	}
	
	// 신청된 상담 상세보기
	@RequestMapping("/alreadyConsultOne.do")
	public String alreadyConsultOne(Principal principal, ConsultVO vo, Model model, ConsultDtVO dtvo, GuideVO gvo) {
		//vo.setGuideId(principal.getName());
		//model.addAttribute("guide", guService.guideSelect(gvo)); // 로그인된 가이드 아이디 확인 (단건조회)
		//model.addAttribute("joblist", cdService.jobCdList()); // 카테고리 공통코드
		model.addAttribute("consultOne", conService.consultSelectOne(vo));
		System.out.println(conService.consultSelectOne(vo));
		model.addAttribute("consultDt", conService.consultDtList(dtvo));
		return "guide/alreadyConsultOne";
	}
	
	// 활성화, 비활성화 기능 - 가이드마이페이지
	@PostMapping("/ajaxConsultState.do")
	@ResponseBody
	public int ajaxConsultState(ConsultVO vo) {
		System.out.println(vo);
		return conService.consultState(vo);
	}
	
	// 채팅방 연결 - 가이드마이페이지
	@GetMapping("/consultChat.do")
	public String consultChat(ConsultVO vo, ConsultChatVO chatvo, Model model, HttpServletRequest request) {
		int consultNo = Integer.parseInt(request.getParameter("consultNo"));
		System.out.println("==========" + consultNo);
		chatvo.setConsultNo(consultNo);
		model.addAttribute("chat", conService.consultChat(chatvo));
		return "chat/consultChat";

	}
	
	//유저 상담 신청 상세 페이지
	@GetMapping("/consultRegister.do")
	public String consultRegister(ConsultVO vo, ConsultDtVO dtvo, PayformVO pvo, Model model, HttpServletRequest request) {
		int consultNo = Integer.parseInt(request.getParameter("consultNo"));
		vo.setConsultNo(consultNo);
		model.addAttribute("consult", conService.consultSelectOne(vo)); //단건조회(신청한 상담)
		dtvo.setConsultNo(consultNo);
		model.addAttribute("consultDT", conService.consultDtList(dtvo)); //시간조회(신청한 상담의)
		model.addAttribute("max", payService.payCount(pvo));
		return "consult/consultRegister";
	}
	
	//유저 상담 신청 시 금액 가져오기
	@PostMapping("/ajaxConsultPrice.do")
	@ResponseBody
	public ConsultDtVO ajaxConsultPrice(ConsultDtVO vo, Model model, HttpServletRequest request) {
		int consultNo = Integer.parseInt(request.getParameter("consultNo")); //상담번호
		String day = request.getParameter("day"); //요일
		System.out.println(consultNo + day);
		vo.setConsultNo(consultNo);
		vo.setWeek(day);
		//conService.consultTime()
		return conService.ajaxConsultPrice(vo);
	}
}
