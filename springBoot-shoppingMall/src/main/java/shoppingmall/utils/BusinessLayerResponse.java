package shoppingmall.utils;

import lombok.Data;

@Data
public class BusinessLayerResponse<T> {

	public enum Status {
		OK("ok"), ERROR("error");

		private String code;

		Status(String code) {
			this.code = code;
		}

		public String code() {
			return code;
		}

	}

	private BusinessLayerResponse(T body, String status, String msg) {
		this.body = body;
		this.status = status;
		this.msg = msg;
	}

	private T body;
	private String status;
	private String msg;

	public static <T> BusinessLayerResponse<T> ok() {
		return new BusinessLayerResponse<>(null, Status.OK.code(), "ok");
	}

	public static <T> BusinessLayerResponse<T> ok(T body) {
		return new BusinessLayerResponse<>(body, Status.OK.code(), "ok");
	}

	public static <T> BusinessLayerResponse<T> ok(T body, String msg) {
		return new BusinessLayerResponse<>(body, Status.OK.code(), msg);
	}

	public static <T> BusinessLayerResponse<T> error(String msg) {
		return new BusinessLayerResponse<>(null, Status.ERROR.code(), msg);
	}
}