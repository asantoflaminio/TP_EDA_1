package AVL;

public class Check {

	private Boolean value;

	public Check(){
		this.value = true;
	}
	
	public Check(boolean value) {
		this.value = value;
	}

	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean b) {
		value = b;
	}
}
