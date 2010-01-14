use File::Basename;
use File::Spec;
use File::Path;

#出力ディレクトリをパッケージ構成と一致させるかどうか（ON=1）
$SYNC_PACKAGE_DIRECTORY = 0;

#「VC too big」をpassしたと見なすかどうか(ON=1)
$IS_VC_TOO_BIG_PASSED = 1;

#--------------------------------------------
# メインルーチン
#--------------------------------------------

#入力ファイル、出力ファイルが指定されていなかったら終了
if (@ARGV < 2)
{
	print "Usage: " . __FILE__ . " input_java_filename output_xml_dir [options for ESC/Java2]\n";
	print "Example1: " . __FILE__ . " TestClass.java ./\n";
	print "Example2: " . __FILE__ . " MyProject.java ./ -classpath workspace\\myproject\n";
	exit;
}

$java_filename = $ARGV[0];	#入力javaファイル名
$xml_dir = $ARGV[1];		#出力ディレクトリ

#ESC/Java2実行時のオプション
$escj_options = join(' ', @ARGV[2..$#ARGV]);

#クラス名（単純名）を取得
$class_simple_name = basename($java_filename, ".java");

#ESC/Java2の実行コマンド
$command = "escj -NoCautions $java_filename $escj_options";	

#ESC/Java2を実行し、出力をescj_resultに格納
open(IN, "$command |");
local $/ = undef;
my $escj_result = <IN>; #改行を含む文字列全体
close(IN);

#ESC/Java2の出力表示
print $escj_result;

#ESC/Java2の出力からパッケージ名、完全限定名を取得
if (!($escj_result =~ m/(\S*)$class_simple_name \.\.\./)) { #「パッケージ名.単純名 ...」
	print "Class name was not found.";
	exit;
}

#パッケージ名（グローバル変数）
$package_name = $1;
if ($package_name ne "") {
	chop($package_name);	
}

#クラス名（完全限定名）
if ($package_name eq "") {
	$class_full_qualified_name = $class_simple_name;
}
else {
	$class_full_qualified_name = $package_name . '.' . $class_simple_name;
}

print "パッケージ=$package_name\n";
print "単純名=$class_simple_name\n";
print "完全限定名=$class_full_qualified_name\n";

#インナークラス名一覧取得
@inner_class_simple_names = $escj_result =~ /$class_full_qualified_name\$(\S+) \.\.\./g;

#出力をインナークラス名で分割
@escj_results = split(/$class_full_qualified_name\$\S+ \.\.\./, $escj_result);

#(Outer)クラスについてXML生成
my %class_data =  &generateClassData($class_simple_name, $class_full_qualified_name, $escj_results[0]);
&OutputXml($xml_dir, \%class_data);

#インナークラスについてXML生成
if (@inner_class_simple_names > 0) {
	my $i = 1;
	foreach my $inner_class_simple_name (@inner_class_simple_names) {
		#print "@@@@@@@@@@@@\n";
		#print "\n[$i]inner_class_name=$inner_class_name\n";
		#print "$escj_results[$i]\n";
		%class_data =  &generateClassData($class_simple_name.'$'.$inner_class_simple_name, $class_full_qualified_name.'$'.$inner_class_simple_name, $escj_results[$i]);
		&OutputXml($xml_dir, \%class_data);
		$i++;
	}
}

#終了
print "Complete.";

#--------------------------------------------
# クラスデータを生成する
#--------------------------------------------
#引数:
# string クラス名(単純名)。
# string クラス名(完全限定名)。
# string ESC/Java2の出力。改行を含む文字列。
#戻り値:
# ハッシュ クラスデータ
#--------------------------------------------
sub generateClassData{
	my ($class_simple_name, $class_full_qualified_name, $escj_result) = @_;
	($class_name_full_qualified_regex = $class_full_qualified_name) =~ s/\$/\\\$/;		#クラス名に$が含まれていたらエスケープ
	my @methods = (); 		#メソッド一覧データ
	my $passed_count = 0; 	#passedしたメソッド数
	
	while ($escj_result =~ m/$class_name_full_qualified_regex: (\S+)\((.*)\) \.\.\./g) #メソッド名(引数)にマッチ。「クラス名: メソッド名 ...」
	{
		# メソッド名
		$method_name = $1;
		
		# 引数
		$method_parameter = $2;
		
		# passed、failed、VC too bigなどの値
		$escj_result =~ m/\[.* s .* bytes\]  (.*)/g;	#「[0.11 s 12234 bytes]  passed」という形
		$method_result = $1;
		if ($method_result eq "passed"){
			$passed_count++;
		}
		elsif ($IS_VC_TOO_BIG_PASSED == 1) { #VC too bigをpassしたと見なす場合
			if ($method_result eq "VC too big") {
				$passed_count++;
			}
		}
		
		# メソッド一覧データに格納
		my $method = {
			'name' => $method_name,
			'result' => $method_result
		};
		if ($method_parameter ne "") {
			$method->{parameter} = $method_parameter;
		}
		push(@methods, $method);
	}

	#クラスデータ生成
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
# XMLファイルを出力する
#--------------------------------------------
#引数:
# string 出力XMLディレクトリ
# ハッシュのアドレス クラスデータ
#--------------------------------------------
sub OutputXml {
	my ($xml_dir, $class_data) = @_;
	my ($class_full_qualified_name, $class_simple_name, $method_count, $passed_count, $methods_ref) 
		= @$class_data{'full_qualified_name', 'simple_name', 'method_count', 'passed_count', 'methods'};
	@methods = @$methods_ref;
	
	print "class_full_qualified_name=$class_full_qualified_name\n";
	print "class_simple_name=$class_simple_name\n";
	#print "methods=@methods\n";
	
	# XMLデータを生成
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

	#xmlファイルを出力
	if ($SYNC_PACKAGE_DIRECTORY == 1) { #出力XMLファイル 「パッケージ\クラス名.xml」
		$xml_dirpath = File::Spec->catfile($xml_dir, split(/\./, $package_name));
		makeDirectory($xml_dirpath);
		$xml_filepath = File::Spec->catfile($xml_dirpath, "$class_simple_name.xml");
	}
	else {	#出力XMLファイル 「パッケージ.クラス名.xml」
		makeDirectory($xml_dir);
		$xml_filepath = File::Spec->catfile($xml_dir, "$class_full_qualified_name.xml");
	}
	open (OUT, ">$xml_filepath");
	print OUT $xml_data;
	close(OUT);
	print "output for \"$xml_filepath\"\n";
}

#--------------------------------------------
# ディレクトリが存在しなければ作成する
#--------------------------------------------
#引数:
# string ディレクトリパス
#--------------------------------------------
sub makeDirectory {
	my ($dir_path) = @_;
	
	if (!-d $dir_path){
		mkpath $dir_path;
	}
}
