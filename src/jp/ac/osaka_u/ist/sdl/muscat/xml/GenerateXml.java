package jp.ac.osaka_u.ist.sdl.muscat.xml;

import java.lang.Runtime;
import java.lang.Process;
import java.util.ArrayList;
import java.io.*;

import jp.ac.osaka_u.ist.sdl.muscat.config.Config;

/**
 * JavaソースファイルからXMLファイルを生成するクラス．
 * 内部クラスは独立したファイルになるため，1つのJavaソースファイルに対して複数のXMLファイルが生成される．
 * @author y-mutoh
 *
 */
public class GenerateXml {
	private final static int EXIT_SUCCESS = 0;
	
	/**
	 * 入力Javaソースファイル．
	 */
	private File javaFile = null;
	
	/**
	 * XML出力先ディレクトリ．
	 */
	private File outputDir = null;
	
	/**
	 * 出力XMLファイル．1つの入力Javaソースファイルに対して複数存在する．
	 */
	private ArrayList<File> xmlFiles = null;
	
	/**
	 * クラスパス．ESC/Java2の実行に用いる．
	 */
	private String classPath;
	
	//デバッグ用．trueのときXMLを生成しない．
	public boolean isMakeXML=true;
	

	public GenerateXml(File javafile, File outputDir, String classpath) 
	{
		this.javaFile = javafile;
		this.classPath = classpath;
		this.outputDir = outputDir;
		try {
			generateXml();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	public GenerateXml(File javafile, File outputDir)
	{
		this.javaFile = javafile;
		this.classPath = null;
		this.outputDir = outputDir;
		try {
			generateXml();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	//デバッグ用．XMLを生成するかどうか設定できる．
	public GenerateXml(File javafile, File outputDir, String classpath, boolean isMakeXML) {
		this.javaFile = javafile;
		this.outputDir = outputDir;
		this.isMakeXML = isMakeXML;
		this.classPath = classpath;
		try {
			generateXml();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	/**
	 * XMLを生成する．
	 * 
	 * @param javafile 入力となるJavaファイル
	 * @param outdir XMLを出力するディレクトリ
	 */
	public void generateXml() throws GenerateXmlException,FileNotFoundException
	{
		if (null == this.javaFile)
			throw new GenerateXmlException("Input Java file is null");
		if (null == this.outputDir)
			throw new GenerateXmlException("XML output directory is null");
		if (!this.javaFile.exists())
			throw new FileNotFoundException(this.javaFile + "is not found");
		if (!this.outputDir.exists())
			throw new FileNotFoundException(this.outputDir + "is not found");
		
		//コマンド生成
		String generatorCommand = Config.getInstance().getGenerateCommand() 
			+ " " + encodeFilePath(this.javaFile.getPath()) 
			+ " " + encodeFilePath(this.outputDir.getPath());
		if (null != this.classPath) {
			generatorCommand  += " -classpath " + encodeFilePath(this.classPath);
		}
		System.out.println("command=" +generatorCommand);
		if (isMakeXML) {
			//スクリプトを実行
			StringBuilder out;
			try {
				out = exec(generatorCommand);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				this.xmlFiles = null;
				return;
			}
			//出力XMLファイルをセット
			ArrayList<File> xmlFiles = new ArrayList<File>();
			String[] outStrs = out.toString().split(System.getProperty("line.separator"));
			for (String line : outStrs) {
				//#で始まる行はコメントとみなす
				if (line.startsWith("#")) continue;
				//出力XMLファイルとして追加
				System.out.println("outfile=" + line);
				xmlFiles.add(new File(line));
			}
			this.xmlFiles = xmlFiles;
		}
	}
	
	/**
	 * コマンドを実行する．	
	 * @param command 実行するコマンド．
	 * @return 標準出力に出力された文字列．
	 */
	private StringBuilder exec(String command)
		throws IOException,InterruptedException, GenerateXmlException, Exception
	{
		StringBuilder out = new StringBuilder();

		//標準出力からとってくる
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(command);
		InputStream is = pr.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String line;
		while ((line = br.readLine()) != null)
		{
			out.append(line + System.getProperty("line.separator")); //改行を付加
			System.out.println(line);
		}
		//終了するまで待つ
		pr.waitFor();
		
		br.close();
		is.close();
		//ステータスチェック
		if (pr.exitValue() != EXIT_SUCCESS) {
			throw new Exception("execute command error. code:" + pr.exitValue());
		}			
		return out;
			
	}
	
	/**
	 * File path encoder.
	 * in current version, only quotes.
	 * @param filepath
	 * @return
	 */
	private String encodeFilePath(String filepath) {
		return "\"" + filepath + "\""; 
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
