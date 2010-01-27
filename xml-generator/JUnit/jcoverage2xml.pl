#JCoverage�ŃG�N�X�|�[�g�������|�[�g(html�t�@�C��)����XML�𐶐�����

use File::Basename;
use File::Spec;
use File::Path;

%ERROR_MESSAGE = (
	'NO_FILE' => 'Input file dont exist.',
	'FORMAT' => 'Unsupported HTML format.'
);

$html_filename = $ARGV[0];	#����html�t�@�C����
$xml_dir = $ARGV[1];		#�o�̓f�B���N�g��

$html_filename = 'C:\Users\y-mutoh\research\StockManagement-djUnit-export\index.html';
$xml_dir = 'C:\Users\y-mutoh\research\StockManagement-djUnit-export\xml';


#html�t�@�C���̓��e��ϐ�$html�Ɋi�[
open(IN, $html_filename);
local $/ = undef;
my $html = <IN>; #���s���܂ޕ�����S��
close(IN);

#<h2>All Java&trade; files</h2>�̂Ƃ���܂Ői�߂�
if (!($html =~ m/<h2>All Java\&trade; files<\/h2>/)) {
	print $ERROR_MESSAGE{'FORMAT'};
	exit(1);
}


#�N���X�̊��S���薼�擾
while ($html =~ m/<td class="filename">\n<a href=\S*>(\S*)<\/a><\/td>/g) { 
	$full_qualified_name = $1;
	
	#line coverage�擾
	$html =~ m/<td class="coverage">\n<a href=$full_qualified_name\.html>(\d*)%<\/a><\/td>/;
	$line_coverage = $1;

	#���S���薼����p�b�P�[�W���ƒP�����𓾂�
	my $package_name;	
	my $simple_name;
	fullQualifiedName2packageNameAndSimpleName($full_qualified_name, \$package_name, \$simple_name);

	#�N���X�f�[�^����
	my %class_data =  (
		'full_qualified_name' => $full_qualified_name,
		'package_name' => $package_name,
		'simple_name' => $simple_name,
		'line_coverage' => $line_coverage
	);
	
	#XML�t�@�C���o��
	&OutputXml($xml_dir, \%class_data);
}

#�I��
exit(0);


#--------------------------------------------
# XML�t�@�C�����o�͂���
#--------------------------------------------
#����:
# string �o��XML�f�B���N�g��
# �n�b�V���̃A�h���X �N���X�f�[�^
#--------------------------------------------
sub OutputXml {
	my ($xml_dir, $class_data) = @_;
	my ($full_qualified_name, $package_name, $simple_name, $line_coverage) 
		= @$class_data{'full_qualified_name', 'package_name', 'simple_name', 'line_coverage'};
	
	# XML�f�[�^�𐶐�
	my $xml_data =<<EndOfXML;
<?xml version="1.0" encoding="utf-8"?>
<class>
	<packageName>$package_name</packageName>
	<simpleName>$simple_name</simpleName>
	<coverage>$line_coverage</coverage>
</class>
EndOfXML
	
	
	#xml�t�@�C�����o��
	if ($SYNC_PACKAGE_DIRECTORY == 1) { #�o��XML�t�@�C�� �u�p�b�P�[�W\�N���X��.xml�v
		$xml_dirpath = File::Spec->catfile($xml_dir, split(/\./, $package_name));
		makeDirectory($xml_dirpath);
		$xml_filepath = File::Spec->catfile($xml_dirpath, "$simple_name.xml");
	}
	else {	#�o��XML�t�@�C�� �u�p�b�P�[�W.�N���X��.xml�v
		makeDirectory($xml_dir);
		$xml_filepath = File::Spec->catfile($xml_dir, "$full_qualified_name.xml");
	}
	open (OUT, ">$xml_filepath");
	print OUT $xml_data;
	close(OUT);
	#�o�̓t�@�C�������o��
	print "$xml_filepath\n";
}

#--------------------------------------------
# ���S���薼�� �p�b�P�[�W���ƒP�����ɕ�������
#--------------------------------------------
#����:
# string ���S���薼
# string�̃A�h���X �p�b�P�[�W���ւ̃|�C���^
# string�̃A�h���X �P�����ւ̃|�C���^
#--------------------------------------------
sub fullQualifiedName2packageNameAndSimpleName {
	my ($full_qualified_name, $package_name, $simple_name) = @_;
	@elements = split(/\./,$full_qualified_name);
	$last_index = $#elements;
	#�P����
	$$simple_name = pop(@elements);
	#�p�b�P�[�W��
	$$package_name = join('.', @elements);
}

#�ȉ�escj2xml.pl����R�s�y

#--------------------------------------------
# �f�B���N�g�������݂��Ȃ���΍쐬����
#--------------------------------------------
#����:
# string �f�B���N�g���p�X
#--------------------------------------------
sub makeDirectory {
	my ($dir_path) = @_;
	
	if (!-d $dir_path){
		mkpath $dir_path;
	}
}

#--------------------------------------------
# �f�o�b�O���̂݃R�����g���o�͂���
#--------------------------------------------
#����:
# string �o�͂������R�����g
#--------------------------------------------
sub debugprint {
	if ($DEBUG == 1) {
		printcomment(@_);
	}
}

#--------------------------------------------
# �R�����g���o�͂���
#--------------------------------------------
#����:
# string �o�͂������R�����g
#--------------------------------------------
sub printcomment {
	my ($arg) = @_;
	@lines = split(/\n/, $arg);
	foreach $line (@lines) {
		print "#$line\n";
	}
}
