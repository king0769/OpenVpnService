package foxit.openvpnservice.management;

public interface EventHandler<T> {
    /**
     * Raise the event
     * 
     * @param sender object that sent the event
     * @param data extra data describing the event
     */
	public abstract void Raise(Object sender, T data);
}
