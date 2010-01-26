use File::Basename;
use File::Spec;
use File::Path;

#�f�o�b�O���[�h(ON=1)
$DEBUG = 1;

#�o�̓f�B���N�g�����p�b�P�[�W�\���ƈ�v�����邩�ǂ����iON=1�j
$SYNC_PACKAGE_DIRECTORY = 0;

#pass�����ƌ��Ȃ��v�f��ݒ�D�Ώە�������L�[�Ƃ��l��1�Ƃ���D
%PASSES = ('passed' => 1
		  ,'VC too big' => 1);

#--------------------------------------------
# ���C�����[�`��
#--------------------------------------------

#���̓t�@�C���A�o�̓t�@�C�����w�肳��Ă��Ȃ�������I��
if (@ARGV < 2)
{
	print "Usage: " . __FILE__ . " input_java_filename output_xml_dir [options for ESC/Java2]\n";
	print "Example1: " . __FILE__ . " TestClass.java ./\n";
	print "Example2: " . __FILE__ . " MyProject.java ./ -classpath workspace\\myproject\n";
	exit(1);
}

$java_filename = $ARGV[0];	#����java�t�@�C����
$xml_dir = $ARGV[1];		#�o�̓f�B���N�g��

#ESC/Java2���s���̃I�v�V����
$escj_options = join(' ', @ARGV[2..$#ARGV]);

#�N���X���i�P�����j���擾
$class_simple_name = basename($java_filename, ".java");

#ESC/Java2�̎��s�R�}���h
$command = "escj -NoCautions $java_filename $escj_options";	

#ESC/Java2�����s���A�o�͂�escj_result�Ɋi�[
open(IN, "$command |");
local $/ = undef;
my $escj_result = <IN>; #���s���܂ޕ�����S��
close(IN);

#ESC/Java2�̏o�͕\��
printcomment($escj_result);

#ESC/Java2�̏o�͂���p�b�P�[�W���A���S���薼���擾
if (!($escj_result =~ m/(\S*)$class_simple_name \.\.\./)) { #�u�p�b�P�[�W��.�P���� ...�v
	print "Class name was not found.";
	exit(1);
}

#�p�b�P�[�W���i�O���[�o���ϐ��j
$package_name = $1;
if ($package_name ne "") {
	chop($package_name);	
}

#�N���X���i���S���薼�j
if ($package_name eq "") {
	$class_full_qualified_name = $class_simple_name;
}
else {
	$class_full_qualified_name = $package_name . '.' . $class_simple_name;
}

debugprint("�p�b�P�[�W=$package_name\n");
debugprint("�P����=$class_simple_name\n");
debugprint("���S���薼=$class_full_qualified_name\n");

#�C���i�[�N���X���ꗗ�擾
@inner_class_simple_names = $escj_result =~ /$class_full_qualified_name\$(\S+) \.\.\./g;

#�o�͂��C���i�[�N���X���ŕ���
@escj_results = split(/$class_full_qualified_name\$\S+ \.\.\./, $escj_result);

#(Outer)�N���X�ɂ���XML����
my %class_data =  &generateClassData($class_simple_name, $class_full_qualified_name, $escj_results[0]);
&OutputXml($xml_dir, \%class_data);

#�C���i�[�N���X�ɂ���XML����
if (@inner_class_simple_names > 0) {
	my $i = 1;
	foreach my $inner_class_simple_name (@inner_class_simple_names) {
		debugprint("@@@@@@@@@@@@\n");
		debugprint("\n[$i]inner_class_name=$inner_class_name\n");
		debugprint("$escj_results[$i]\n");
		%class_data =  &generateClassData($class_simple_name.'$'.$inner_class_simple_name, $class_full_qualified_name.'$'.$inner_class_simple_name, $escj_results[$i]);
		&OutputXml($xml_dir, \%class_data);
		$i++;
	}
}

#�I��
debugprint("Complete.");
exit(0); #����I��

#--------------------------------------------
# �N���X�f�[�^�𐶐�����
#--------------------------------------------
#����:
# string �N���X��(�P����)�B
# string �N���X��(���S���薼)�B
# string ESC/Java2�̏o�́B���s���܂ޕ�����B
#�߂�l:
# �n�b�V�� �N���X�f�[�^
#--------------------------------------------
sub generateClassData{
	my ($class_simple_name, $class_full_qualified_name, $escj_result) = @_;
	($class_name_full_qualified_regex = $class_full_qualified_name) =~ s/\$/\\\$/;		#�N���X����$���܂܂�Ă�����G�X�P�[�v
	my @methods = (); 		#���\�b�h�ꗗ�f�[�^
	my $passed_count = 0; 	#passed�������\�b�h��
	
	while ($escj_result =~ m/$class_name_full_qualified_regex: (\S+)\((.*)\) \.\.\./g) #���\�b�h��(����)�Ƀ}�b�`�B�u�N���X��: ���\�b�h�� ...�v
	{
		# ���\�b�h��
		$method_name = $1;
		
		# ����
		$method_parameter = $2;
		
		# passed�Afailed�AVC too big�Ȃǂ̒l
		$escj_result =~ m/\[.* s .* bytes\]  (.*)/g;	#�u[0.11 s 12234 bytes]  passed�v�Ƃ����`
		$method_result = $1;

		#pass�������ǂ�������
		if ($PASSES{$method_result} == 1) {
			$passed_count++;
		}
			
		# ���\�b�h�ꗗ�f�[�^�Ɋi�[
		my $method = {
			'name' => $method_name,
			'result' => $method_result
		};
		if ($method_parameter ne "") {
			$method->{parameter} = $method_parameter;
		}
		push(@methods, $method);
	}

	#�N���X�f�[�^����
	my $method_count = @methods;
	return (
		'full_qualified_name' => $class_full_qualified_name,
		'simple_name' => $class_simple_name,
		'method_count' => $method_count,
		'passed_count' => $passed_count,
		'coverage' => $coverage,
		'methods' => \@methods
	);
}


#--------------------------------------------
# XML�t�@�C�����o�͂���
#--------------------------------------------
#����:
# string �o��XML�f�B���N�g��
# �n�b�V���̃A�h���X �N���X�f�[�^
#--------------------------------------------
sub OutputXml {
	my ($xml_dir, $class_data) = @_;
	my ($class_full_qualified_name, $class_simple_name, $method_count, $passed_count, $methods_ref) 
		= @$class_data{'full_qualified_name', 'simple_name', 'method_count', 'passed_count', 'methods'};
	@methods = @$methods_ref;
	debugprint("class_full_qualified_name=$class_full_qualified_name\n");
	debugprint("class_simple_name=$class_simple_name\n");
	#debugprint "methods=@methods\n";
	
	# XML�f�[�^�𐶐�
	my $xml_data =<<EndOfHeader;
<?xml version="1.0" encoding="utf-8"?>
<class>
	<packageName>$package_name</packageName>
	<simpleName>$class_simple_name</simpleName>
	<methodCount>$method_count</methodCount>
	<passedCount>$passed_count</passedCount>
	<methods>
EndOfHeader

	my $i=1; #id
	foreach my $m (@methods) {
		$xml_data .=<<EndOfMethod1;
		<method id="$i">
			<name>$m->{name}</name>
EndOfMethod1
		if (exists $m->{parameter}) {
			$xml_data .= "\t\t\t<parameter>$m->{parameter}</parameter>\n";
		}
		$xml_data .=<<EndOfMethod2;
			<attributes>
				<attribute>
					<title>passed/failed</title>
					<value>$m->{result}</value>
				</attribute>
			</attributes>
		</method>
EndOfMethod2
		$i++;
	}

$xml_data .=<<EndOfFooter;
	</methods>
</class>
EndOfFooter

	#xml�t�@�C�����o��
	if ($SYNC_PACKAGE_DIRECTORY == 1) { #�o��XML�t�@�C�� �u�p�b�P�[�W\�N���X��.xml�v
		$xml_dirpath = File::Spec->catfile($xml_dir, split(/\./, $package_name));
		makeDirectory($xml_dirpath);
		$xml_filepath = File::Spec->catfile($xml_dirpath, "$class_simple_name.xml");
	}
	else {	#�o��XML�t�@�C�� �u�p�b�P�[�W.�N���X��.xml�v
		makeDirectory($xml_dir);
		$xml_filepath = File::Spec->catfile($xml_dir, "$class_full_qualified_name.xml");
	}
	open (OUT, ">$xml_filepath");
	print OUT $xml_data;
	close(OUT);
	#�o�̓t�@�C�������o��
	print "$xml_filepath\n";
}

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


