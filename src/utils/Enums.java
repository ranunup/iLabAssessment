package utils;

public class Enums {
	public enum SelectorType {
		css, xpath, id, name, tagName, linkText, className
	}
	
	public enum BrowserType {
		chrome, firefox, ie
	}
	
	public enum ExecTestFlag {
		Y, N
	}
	public enum Operation {
		
		clickButton(""), clickByHrefText(""), clickHrefPositionAfterText(""), emailLogin(""),
		insertCellNoById(""), insertById(""), insertTextById(""), navigateTo(""),
		usernameLogin(""), validateErrorText(""), validateH1Text(""), validateText(""), verifyTitle("");
		
		private String operationValue;
		
		Operation(String operationValue) {
			this.operationValue = operationValue;
		}
		
		public String getOperationValue() {
			return this.operationValue;
		}
		
		public void setOperationValue(String operationValue) {
			this.operationValue = operationValue;
		}
	}
}
