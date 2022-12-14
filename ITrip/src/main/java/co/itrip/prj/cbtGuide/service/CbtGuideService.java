package co.itrip.prj.cbtGuide.service;

import java.util.List;
import java.util.Map;


/**
* 가이드CBT 구현시 필요한 기능을 기재
* @author 김하은
* @date 2022.09.16
* @version 1.9
*/
public interface CbtGuideService {
	

	
	List<CbtGuideVO> cbtGuideList();           /* 가이드가 등록한 모든 문제 출력 */
	List<CbtGuideVO> cbtGuideMyList(CbtGuideVO vo);         /* 가이드 한 명의 등록한 문제 출력 */
	CbtGuideVO cbtGuideListOne(CbtGuideVO vo); /* 문제 한 건 조회 */
	
	List<CbtGuideVO> cbtGuideListTab(CbtGuideVO vo); /* 문제 조회 : 공통코드 (유형, 언어) 필터를 거쳐 출력 */
	List<Map<Integer,Object>> cbtGuideListFive(MyCbtHderVO vo); /* 사용자가 푼제 5개 조회 */ 
	MyCbtHderVO myCbtHderList(MyCbtHderVO vo); /* 내가 푼 문제 조회 */
	public List<CbtGuideVO> cbtGuideListO(CbtGuideVO vo); /* 문제 조회 : 사용자가 푼 문제 중 정답처리 된 문제조회 */
	public List<CbtGuideVO> cbtGuideListX(CbtGuideVO vo); /* 문제 조회 : 사용자가 푼 문제 중 정답처리 된 오답조회 */
	public List<CbtKeywordVO> keywordList(CbtKeywordVO vo); /* 키워드 조회 */
	List<CbtGuideVO> bookmarkList(CbtGuideVO vo); /* 즐겨찾기 조회 */
	
	public int cbtGuideInsert(CbtGuideVO vo);   /* 문제 등록 */
	public int cbtGuideUpdate(CbtGuideVO vo);   /* 문제 수정 */
	
	public int cbutGuideDelet(CbtGuideVO vo);   /* 문제 삭제 */
	public int myCbtHderInsert(MyCbtHderVO vo); /* 사용자가 푼 문제 등록 */
	
	
	public MyCbtLongVO ajaxMyCbtLongList(MyCbtLongVO myVo); /* 객관식 문제 채점시 ajax로 정답 출력 */
	public int ajaxMyCbtLongInsert(MyCbtLongVO vo); /* 사용자가 푼 서술형 문제 등록 */
	public int ajaxBookmarkInsert(BookmarkVO vo);   /* 즐겨찾기 추가 */ 
	public List<Integer> ajaxBookmarkCount(BookmarkVO vo);    /* 즐겨찾기 카운트 */
	public int ajaxBookmarkDelete(BookmarkVO vo);   /* 즐겨찾기 삭제 */ 
	
}
