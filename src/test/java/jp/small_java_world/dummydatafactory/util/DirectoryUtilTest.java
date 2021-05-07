	package jp.small_java_world.dummydatafactory.util;
	
	import static org.junit.jupiter.api.Assertions.assertEquals;
	
	import java.io.File;
	import java.io.IOException;
	import java.net.URL;
	import java.nio.file.Files;
	import java.nio.file.Path;
	
	import org.junit.jupiter.params.ParameterizedTest;
	import org.junit.jupiter.params.provider.ValueSource;
	
	class DirectoryUtilTest {
		@ParameterizedTest
		@ValueSource(strings = { "hoge1", "hoge2/fuga" })
		void testGetPath(String dirNameParam) throws IOException {
			URL url = DirectoryUtil.class.getClassLoader().getResource(".");
			var rootPath = url.getPath();
			var dirName = dirNameParam.replace("/", File.separator);
			
			//Windowsの場合は/c:のようなパスになるので、Path.ofなどがうまく動作しないので先頭の/を除去
			if (SystemUtil.isWindows() && rootPath.startsWith("/")) {
				rootPath = rootPath.substring(1, rootPath.length());
			}
	
			//DirectoryUtil.getPathがパスを検出するパスを作成
			var targetDir = rootPath + ".." + File.separator + ".." + File.separator + dirName;
			
			//targetDirが存在しない場合は作成
			if(!Files.exists(Path.of(targetDir))) {
				//targetDirに対応するディレクトリを作成
				Files.createDirectories(Path.of(targetDir));
			}
	
			var result = DirectoryUtil.getPath(dirName);
			
			//rootPathはテスト実行環境に依存するので、resultのrootPathを$rootPathに置換
			result = result.replace(rootPath, "$rootPath");
	
			//期待値も$rootPathからの相対パスで宣言
			var expectedResult = "$rootPath" + ".." + File.separator + ".." + File.separator + dirName;
			assertEquals(expectedResult, result);
		}
	}
