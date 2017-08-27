<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>订单详细</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/order/desc.css'/>">
  </head>
  
<body>
	<div class="divOrder">
		<span>订单号：${order.oid }
			(等待付款)
<!-- 
			(准备发货)
			(等待确认)
			(交易成功)
			(已取消)
 -->
		　　　下单时间：<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${order.ordertime }"/></span>
	</div>
	<div class="divContent">
		<div class="div2">
			<dl>
				<dt>收货人信息</dt>
				<dd>北京市 昌平区 西三旗 金燕龙办公楼1层 传智播客 张三爷</dd>
			</dl>
		</div>
		<div class="div2">
			<dl>
				<dt>商品清单</dt>
				<dd>
					<table cellpadding="0" cellspacing="0">
						<tr>
							<th class="tt">商品名称</th>
							<th class="tt" align="left">单价</th>
							<th class="tt" align="left">数量</th>
							<th class="tt" align="left">小计</th>
						</tr>


<c:forEach items="${order.orderItemList }" var="orderItem">

						<tr style="padding-top: 20px; padding-bottom: 20px;">
							<td class="td" width="400px">
								<div class="bookname">
								  <img align="middle" width="70" src="<c:url value='/${orderItem.book.image }'/>"/>
								  <a href="<c:url value='/jsps/book/desc.jsp'/>">${orderItem.book.bname }</a>
								</div>
							</td>
							<td class="td" >
								<span>&yen;${orderItem.book.price }</span>
							</td>
							<td class="td">
								<span>${orderItem.count }</span>
							</td>
							<td class="td">
								<span>&yen;${orderItem.subtotal }</span>
							</td>			
						</tr>




</c:forEach>


					</table>
				</dd>
			</dl>
		</div>
		<div style="margin: 10px 10px 10px 550px;">
			<span style="font-weight: 900; font-size: 15px;">合计金额：</span>
			<span class="price_t">&yen;${order.total }</span><br/>

	<a href="<c:url value='/jsps/order/pay.jsp'/>" class="pay"></a><br/>
    <a id="cancel" href="javascript:alert('订单已取消！');">取消订单</a><br/>
	<a id="confirm" href="javascript:alert('交易成功！');">确认收货</a><br/>	
		</div>
	</div>
</body>
</html>

