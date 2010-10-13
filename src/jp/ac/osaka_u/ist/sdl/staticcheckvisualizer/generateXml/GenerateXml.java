package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.generateXml;

import java.lang.Runtime;
import java.lang.Process;
import java.util.ArrayList;
import java.io.*;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.config.Config;

/**
 * JavaソースファイルからXMLファイルを生成するクラス．
 * 内部クラスは独立したファイルになるため，1つのJavaソースファイルに対して複数のXMLファイルが生成される．
 * @author y-mutoh
 *
 */
public class GenerateXml {
	private final static int EXIT_SUCCESS = 0;
	private final static int EXIT_FAILURE = 1;
	
	/**
	 * 入力Javaソースファイル．
	 */
	private File javaFile;
	
	/**
	 * 出力XMLファイル．1つの入力Javaソースファイルに対して複数存在する．
	 */
	private ArrayList<File> xmlFiles;
	
	/**
	 * クラスパス．ESC/Java2の実行に用いる．
	 */
	private String classPath;
	
	//デバッグ用．trueのときXMLを生成しない．
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
	
	//デバッグ用．XMLを生成するかどうか設定できる．
	public GenerateXml(File javafile, String outputdir, String classpath, boolean isMakeXML) {
		this.isMakeXML = isMakeXML;
		this.classPath = classpath;
		generateXml(javafile, outputdir);
	}
	
	/* メソッド */
	
	/**
	 * XMLを生成する．
	 * 
	 * @param javafile 入力となるJavaファイル
	 * @param outdir XMLを出力するディレクトリ
	 */
	public void generateXml(File javafile, String outdir)
	{	
		this.javaFile = javafile;
		//出力先ディレクトリは\で終わるように
		if (!outdir.endsWith("\\")) outdir += "\\";
		//コマンド生成
		String generatorCommand = Config.getInstance().getGenerateCommand() + " " + javaFile.getPath() + " " + outdir;
		if (this.classPath != null) {
			generatorCommand  += " -classpath " + this.classPath;
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
			String[] outStrs = out.toString().split("\n");
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
	private StringBuilder exec(String command) throws Exception
	{
		StringBuilder out = new StringBuilder();
		Runtime rt = Runtime.getRuntime();
		InputStream is;
		Process pr;
		BufferedReader br;
//		try {
			//プロセス生成
			pr = rt.exec(command);
			//標準出力
			is = pr.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null)
			{
				out.append(line + "\n");
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
			
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			return null;
//		}
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
