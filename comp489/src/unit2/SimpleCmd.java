package unit2;

public abstract class SimpleCmd {
	protected String arg;

	public SimpleCmd(String inArg) {
		arg = inArg;
	}

	public abstract String Do();
}

class GetCmd extends SimpleCmd {

	public GetCmd(String inArg) {
		super(inArg);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String Do() {
		String result = arg + " Gotten\n";
		return result;
	}
}

class HeadCmd extends SimpleCmd{

	public HeadCmd(String inArg) {
		super(inArg);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String Do() {
		String result = "Head \"" + arg + "\" processed.\n";
		return result;
	}
}

class PostCmd extends SimpleCmd{

	public PostCmd(String inArg) {
		super(inArg);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String Do() {
		String result = arg + " Posted\n";
		return result;
	}
}

class DoneCmd extends SimpleCmd{

	public DoneCmd(String inArg) {
		super(inArg);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String Do() {
		String result = "All done.\n";
		return result;
	}

}