package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.generateXml;

import java.lang.Runtime;
import java.lang.Process;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.Activator;

/**
 * Java�\�[�X�t�@�C������XML�t�@�C���𐶐�����N���X�B
 * �����N���X�͓Ɨ������t�@�C���ɂȂ邽�߁A1��Java�\�[�X�t�@�C���ɑ΂��ĕ�����XML�t�@�C�������������B
 * @author y-mutoh
 *
 */
public class GenerateXml {
	private final int EXIT_SUCCESS = 0;
	private final int EXIT_FAILURE = 1;
	
	/**
	 * ����Java�\�[�X�t�@�C���B
	 */
	private File javaFile;
	
	/**
	 * �o��XML�t�@�C���B1�̓���Java�\�[�X�t�@�C���ɑ΂��ĕ������݂���B
	 */
	private ArrayList<File> xmlFiles;
	
	/**
	 * �N���X�p�X�BESC/Java2�̎��s�ɗp����B
	 */
	private String classPath;
	
	//�f�o�b�O�p�Btrue�̂Ƃ�XML�𐶐����Ȃ��B
	public boolean isMakeXML=true;
	
	/* �R���X�g���N�^ */ 
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
	
	//�f�o�b�O�p�BXML�𐶐����邩�ǂ����ݒ�ł���B
	public GenerateXml(File javafile, String outputdir, String classpath, boolean isMakeXML) {
		this.isMakeXML = isMakeXML;
		this.classPath = classpath;
		generateXml(javafile, outputdir);
	}
	
	/* ���\�b�h */
	
	/**
	 * XML�𐶐�����B
	 * 
	 * @param javafile ���͂ƂȂ�Java�t�@�C��
	 * @param outputdir XML���o�͂���f�B���N�g��
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
			//�X�N���v�g�����s
			StringBuilder out = exec(generatorCommand);
			//�o��XML�t�@�C�����Z�b�g
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
	 * �R�}���h�����s����B	
	 * @param command ���s����R�}���h�B
	 * @return �W���o�͂ɏo�͂��ꂽ������B
	 */
	private StringBuilder exec(String command)
	{
		StringBuilder out = new StringBuilder();
		Runtime rt = Runtime.getRuntime();
		Process pr;
		try {
			//�v���Z�X����
			pr = rt.exec(command);

			//�W���o��
			InputStream is = pr.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null)
			{
				out.append(line);
				System.out.println(line);
			}
			//�I������܂ő҂�
			pr.waitFor();
			//�X�e�[�^�X�`�F�b�N
			if (pr.exitValue() != EXIT_SUCCESS)
				throw new Exception("execute command error. code:" + pr.exitValue());
			return out;
			
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}
	
	/**
	 * ���K�\����p�����t�B���^��Ԃ��B
	 * @param regex ���K�\���B
	 * @return �t�@�C�����t�B���^�B
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
