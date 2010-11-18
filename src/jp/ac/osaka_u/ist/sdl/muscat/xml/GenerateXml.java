package jp.ac.osaka_u.ist.sdl.muscat.xml;

import java.lang.Runtime;
import java.lang.Process;
import java.util.ArrayList;
import java.io.*;

import jp.ac.osaka_u.ist.sdl.muscat.config.Config;

/**
 * Java�\�[�X�t�@�C������XML�t�@�C���𐶐�����N���X�D
 * �����N���X�͓Ɨ������t�@�C���ɂȂ邽�߁C1��Java�\�[�X�t�@�C���ɑ΂��ĕ�����XML�t�@�C�������������D
 * @author y-mutoh
 *
 */
public class GenerateXml {
	private final static int EXIT_SUCCESS = 0;
	
	/**
	 * ����Java�\�[�X�t�@�C���D
	 */
	private File javaFile = null;
	
	/**
	 * XML�o�͐�f�B���N�g���D
	 */
	private File outputDir = null;
	
	/**
	 * �o��XML�t�@�C���D1�̓���Java�\�[�X�t�@�C���ɑ΂��ĕ������݂���D
	 */
	private ArrayList<File> xmlFiles = null;
	
	/**
	 * �N���X�p�X�DESC/Java2�̎��s�ɗp����D
	 */
	private String classPath;
	
	//�f�o�b�O�p�Dtrue�̂Ƃ�XML�𐶐����Ȃ��D
	public boolean isMakeXML=true;
	

	public GenerateXml(File javafile, File outputDir, String classpath) 
	{
		this.javaFile = javafile;
		this.classPath = classpath;
		this.outputDir = outputDir;
		try {
			generateXml();
		} catch (Exception e) {
			// TODO �����������ꂽ catch �u���b�N
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
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}
	}
	
	//�f�o�b�O�p�DXML�𐶐����邩�ǂ����ݒ�ł���D
	public GenerateXml(File javafile, File outputDir, String classpath, boolean isMakeXML) {
		this.javaFile = javafile;
		this.outputDir = outputDir;
		this.isMakeXML = isMakeXML;
		this.classPath = classpath;
		try {
			generateXml();
		} catch (Exception e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}
	}
	
	/**
	 * XML�𐶐�����D
	 * 
	 * @param javafile ���͂ƂȂ�Java�t�@�C��
	 * @param outdir XML���o�͂���f�B���N�g��
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
		
		//�R�}���h����
		String generatorCommand = Config.getInstance().getGenerateCommand() 
			+ " " + encodeFilePath(this.javaFile.getPath()) 
			+ " " + encodeFilePath(this.outputDir.getPath());
		if (null != this.classPath) {
			generatorCommand  += " -classpath " + encodeFilePath(this.classPath);
		}
		System.out.println("command=" +generatorCommand);
		if (isMakeXML) {
			//�X�N���v�g�����s
			StringBuilder out;
			try {
				out = exec(generatorCommand);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				this.xmlFiles = null;
				return;
			}
			//�o��XML�t�@�C�����Z�b�g
			ArrayList<File> xmlFiles = new ArrayList<File>();
			String[] outStrs = out.toString().split(System.getProperty("line.separator"));
			for (String line : outStrs) {
				//#�Ŏn�܂�s�̓R�����g�Ƃ݂Ȃ�
				if (line.startsWith("#")) continue;
				//�o��XML�t�@�C���Ƃ��Ēǉ�
				System.out.println("outfile=" + line);
				xmlFiles.add(new File(line));
			}
			this.xmlFiles = xmlFiles;
		}
	}
	
	/**
	 * �R�}���h�����s����D	
	 * @param command ���s����R�}���h�D
	 * @return �W���o�͂ɏo�͂��ꂽ������D
	 */
	private StringBuilder exec(String command)
		throws IOException,InterruptedException, GenerateXmlException, Exception
	{
		StringBuilder out = new StringBuilder();

		//�W���o�͂���Ƃ��Ă���
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(command);
		InputStream is = pr.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String line;
		while ((line = br.readLine()) != null)
		{
			out.append(line + System.getProperty("line.separator")); //���s��t��
			System.out.println(line);
		}
		//�I������܂ő҂�
		pr.waitFor();
		
		br.close();
		is.close();
		//�X�e�[�^�X�`�F�b�N
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
	
	/* �A�N�Z�T */
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
