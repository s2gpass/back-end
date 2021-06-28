package com.kt.safe2go.platform.common.log;

import com.kt.safe2go.platform.common.log.vo.TransactionLogVo;
import com.kt.safe2go.platform.common.log.vo.TransactionThreadLocal;
import com.kt.safe2go.platform.common.log.vo.TransactionVo;

public class ThreadRepository {

	
	public static ThreadLocal<TransactionThreadLocal> transactionThreadLocal = new ThreadLocal<TransactionThreadLocal>() {
		@Override
		public TransactionThreadLocal initialValue() {
			return new TransactionThreadLocal();
		};
	};
	
	public static TransactionThreadLocal getTransactionThreadLocal() {
		return transactionThreadLocal.get();
	}
	
	public static void setTransactionThreadLocal(TransactionThreadLocal transaction) {
		transactionThreadLocal.set(transaction);
	}
	
	public static void setTransactionThreadLocal(String requestBody) {
		TransactionThreadLocal transaction = getTransactionThreadLocal();
		transaction.setRequestBody(requestBody);
		transactionThreadLocal.set(transaction);
	}
	public static void setTransactionThreadLocal(TransactionLogVo logVo, TransactionVo vo) {
		TransactionThreadLocal transaction = getTransactionThreadLocal();
		transaction.setLogVo(logVo);
		transaction.setVo(vo);
		transactionThreadLocal.set(transaction);
	}
	
	public static void remove() {
		transactionThreadLocal.remove();
	}
	
}
