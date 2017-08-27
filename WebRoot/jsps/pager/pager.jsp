<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
	function _go() {
		var pc = $("#pageCode").val();//获取文本框中的当前页码
		if(!/^[1-9]\d*$/.test(pc)) {//对当前页码进行整数校验
			alert('请输入正确的页码！');
			return;
		}
		if(pc > 10) {//判断当前页码是否大于最大页
			alert('请输入正确的页码！');
			return;
		}
		location = "";
	}
</script>


<div class="divBody">
  <div class="divContent">
    <%--上一页 --%>
	        <span class="spanBtnDisabled">上一页</span>
		<c:if test="${pb.pc>1 }">
	        <a href="${pb.url }&pc=${pb.pc-1 }" class="aBtn bold">上一页</a>
		</c:if>
    
    <%-- 计算begin和end --%>
      <%-- 如果总页数<=6，那么显示所有页码，即begin=1 end=${pb.tp} --%>
        <%-- 设置begin=当前页码-2，end=当前页码+3 --%>
          <%-- 如果begin<1，那么让begin=1 end=6 --%>
          <%-- 如果end>最大页，那么begin=最大页-5 end=最大页 --%>
	<c:choose>
		<c:when test="${pb.tp<= 6 }">
			<c:set var="begin" value="1"></c:set>
			<c:set var="end" value="${pb.tp }"></c:set>
		</c:when>
		<c:otherwise>
			<!-- 计算公式 -->
			<c:set var="begin" value="${pb.pc-3 }"></c:set>
			<c:set var="end" value="${pb.pc+2 }"></c:set>
			<!-- 头益出 -->
			<c:if test="${begin<1 }">
				<c:set var="begin" value="1"></c:set>
				<c:set var="end" value="10"></c:set>
			</c:if>
			<!-- 尾溢出 -->
			<c:if test="${end>pb.tp }">
				<c:set var="begin" value="${pb.tp-5 }"></c:set>
				<c:set var="end" value="${pb.tp }"></c:set>
			</c:if>
		</c:otherwise>
	</c:choose>
    
    <!-- 循环遍历页码 -->
    <c:forEach var="i" begin="${begin }" end="${end }">
    	<c:choose>
    		<c:when test="${i eq pb.pc }">
    			[${i }]
    		</c:when>
    		<c:otherwise>
		    	<a href="${pb.url }&pc=${ i}" >[${i }]</a>
    		</c:otherwise>
    	</c:choose>
    
    </c:forEach>
    
    
    
    
<!--     显示页码列表  -->
<%--    		 <span class="spanBtnSelect">${i }</span>
          <a href="" class="aBtn">2</a>
          <a href="" class="aBtn">3</a>
          <a href="" class="aBtn">4</a>
          <a href="" class="aBtn">5</a>
          <a href="" class="aBtn">6</a>
	
    
    <%-- 显示点点点 --%>
      <span class="spanApostrophe">...</span> 

    
     <%--下一页 --%>
        <span class="spanBtnDisabled">下一页</span>
     	<c:if test="${pb.pc < pb.tp }">
        <a href="${pb.url }&pc=${pb.pc+1}" class="aBtn bold">下一页</a> 
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    	</c:if>
    <%-- 共N页 到M页 --%>
   第${pb.pc }页/ <span>共${pb.tp }页</span>
    <span>到</span>
    <input type="text" class="inputPageCode" id="pageCode" value="1"/>
    <span>页</span>
    <a href="javascript:_go();" class="aSubmit">确定</a>
  </div>
</div>