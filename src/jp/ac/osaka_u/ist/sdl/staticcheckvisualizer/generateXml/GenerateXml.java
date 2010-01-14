package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.generateXml;

import java.lang.Runtime;
import java.lang.Process;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.Activator;

/**
 * JavaソースファイルからXMLファイルを生成するクラス。
 * 内部クラスは独立したファイルになるため、1つのJavaソースファイルに対して複数のXMLファイルが生成される。
 * @author y-mutoh
 *
 */
public class GenerateXml {
	private final int EXIT_SUCCESS = 0;
	private final int EXIT_FAILURE = 1;
	
	/**
	 * 入力Javaソースファイル。
	 */
	private File javaFile;
	
	/**
	 * 出力XMLファイル。1つの入力Javaソースファイルに対して複数存在する。
	 */
	private ArrayList<File> xmlFiles;
	
	/**
	 * クラスパス。ESC/Java2の実行に用いる。
	 */
	private String classPath;
	
	//デバッグ用。trueのときXMLを生成しない。
	public boolean isMakeXML=true;
	
	/* コンストラクタ */ 
	public GenerateXml(File javafile, String outputdir, String classpath) 
	{
		this.classPath = classpath;
		generateXml(javafile, outputdir);
	}
	
	public GenerateXml(File javafile, String outputdir)
	{
		this.classPath = "";
		generateXml(javafile, outputdir);
	}
	
	//デバッグ用。XMLを生成するかどうか設定できる。
	public GenerateXml(File javafile, String outputdir, String classpath, boolean isMakeXML) {
		this.isMakeXML = isMakeXML;
		this.classPath = classpath;
		generateXml(javafile, outputdir);
	}
	
	/* メソッド */
	
	/**
	 * XMLを生成する。
	 * 
	 * @param javafile 入力となるJavaファイル
	 * @param outputdir XMLを出力するディレクトリ
	 */
	public void generateXml(File javafile, String outputdir)
	{	
		this.javaFile = javafile;
		String generatorCommand = Activator.getConfig().getGenerateCommand() + " " + javaFile.getPath() + " " + outputdir;
		System.out.println("command=" +generatorCommand);
		if (this.classPath != null) {
			generatorCommand  += " -classpath " + this.classPath;
		}
		if (isMakeXML) {
			//スクリプトを実行
			StringBuilder out = exec(generatorCommand);
			//出力XMLファイルをセット
			ArrayList<File> xmlFiles = new ArrayList<File>();
			String[] outStrs = out.toString().split("\n");
			System.out.println("out="+outStrs);
			for (String filename:outStrs) {
				xmlFiles.add(new File(filename));
			}
			this.xmlFiles = xmlFiles;
		}
	}
	
	/**
	 * コマンドを実行する。	
	 * @param command 実行するコマンド。
	 * @return 標準出力に出力された文字列。
	 */
	private StringBuilder exec(String command)
	{
		StringBuilder out = new StringBuilder();
		Runtime rt = Runtime.getRuntime();
		Process pr;
		try {
			//プロセス生成
			pr = rt.exec(command);

			//標準出力
			InputStream is = pr.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null)
			{
				out.append(line);
				System.out.println(line);
			}
			//終了するまで待つ
			pr.waitFor();
			//ステータスチェック
			if (pr.exitValue() != EXIT_SUCCESS)
				throw new Exception("execute command error. code:" + pr.exitValue());
			return out;
			
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}
	
	/**
	 * 正規表現を用いたフィルタを返す。
	 * @param regex 正規表現。
	 * @return ファイル名フィルタ。
	 */
	private static FilenameFilter getFileRegexFilter(String regex) {
		final String regex_ = regex;
		return new FilenameFilter() {
			public boolean accept(File file, String name) {
				boolean ret = name.matches(regex_);
				return ret;
			}
		};
	}
	
	/* アクセサ */
	public File getJavaFile() {
		return javaFile;
	}

	public void setJavaFile(File javaFile) {
		this.javaFile = javaFile;
	}

	public ArrayList<File> getXmlFiles() {
		return xmlFiles;
	}
	


}
