package main.java.jSwingComponentMenuTree;

public enum MenuOptionCaption {
	INSTALLATION("Installation"); //

	protected final String label;

	private MenuOptionCaption(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
