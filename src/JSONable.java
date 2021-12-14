import org.json.simple.JSONObject;

public abstract class JSONable {
	public abstract JSONObject toJSONObject();
	public String toJSONString() {
		return this.toJSONObject().toJSONString();
	}
}
