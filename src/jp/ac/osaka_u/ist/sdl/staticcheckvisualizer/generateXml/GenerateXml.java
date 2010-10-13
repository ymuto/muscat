package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.generateXml;

import java.lang.Runtime;
import java.lang.Process;
import java.util.ArrayList;
import java.io.*;

import jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.config.Config;

/**
 * Java�\�[�X�t�@�C������XML�t�@�C���𐶐�����N���X�D
 * �����N���X�͓Ɨ������t�@�C���ɂȂ邽�߁C1��Java�\�[�X�t�@�C���ɑ΂��ĕ�����XML�t�@�C�������������D
 * @author y-mutoh
 *
 */
public class GenerateXml {
	private final static int EXIT_SUCCESS = 0;
	private final static int EXIT_FAILURE = 1;
	
	/**
	 * ����Java�\�[�X�t�@�C���D
	 */
	private File javaFile;
	
	/**
	 * �o��XML�t�@�C���D1�̓���Java�\�[�X�t�@�C���ɑ΂��ĕ������݂���D
	 */
	private ArrayList<File> xmlFiles;
	
	/**
	 * �N���X�p�X�DESC/Java2�̎��s�ɗp����D
	 */
	private String classPath;
	
	//�f�o�b�O�p�Dtrue�̂Ƃ�XML�𐶐����Ȃ��D
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
	
	//�f�o�b�O�p�DXML�𐶐����邩�ǂ����ݒ�ł���D
	public GenerateXml(File javafile, String outputdir, String classpath, boolean isMakeXML) {
		this.isMakeXML = isMakeXML;
		this.classPath = classpath;
		generateXml(javafile, outputdir);
	}
	
	/* ���\�b�h */
	
	/**
	 * XML�𐶐�����D
	 * 
	 * @param javafile ���͂ƂȂ�Java�t�@�C��
	 * @param outdir XML���o�͂���f�B���N�g��
	 */
	public void generateXml(File javafile, String outdir)
	{	
		this.javaFile = javafile;
		//�o�͐�f�B���N�g����\�ŏI���悤��
		if (!outdir.endsWith("\\")) outdir += "\\";
		//�R�}���h����
		String generatorCommand = Config.getInstance().getGenerateCommand() + " " + javaFile.getPath() + " " + outdir;
		if (this.classPath != null) {
			generatorCommand  += " -classpath " + this.classPath;
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
			String[] outStrs = out.toString().split("\n");
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
	private StringBuilder exec(String command) throws Exception
	{
		StringBuilder out = new StringBuilder();
		Runtime rt = Runtime.getRuntime();
		InputStream is;
		Process pr;
		BufferedReader br;
//		try {
			//�v���Z�X����
			pr = rt.exec(command);
			//�W���o��
			is = pr.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null)
			{
				out.append(line + "\n");
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
			
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			return null;
//		}
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
