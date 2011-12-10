/*
 * Copyright 2011 PrimeFaces Extensions.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id: YuiCompressorOptimizer.java 513 2011-12-01 18:58:10Z ovaraksin@googlemail.com $
 */

package org.primefaces.extensions.optimizerplugin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;

import com.google.common.io.Files;

import com.yahoo.platform.yui.compressor.CssCompressor;

/**
 * Class for YUI Compressor doing CSS optimization.
 *
 * @author  Oleg Varaksin / last modified by $Author: ovaraksin@googlemail.com $
 * @version $Revision: 513 $
 * @since   0.1
 */
public class YuiCompressorOptimizer extends AbstractOptimizer {

	private static final String OPTIMIZED_FILE_EXTENSION = ".optcss";

	@Override
	public void optimize(final ResourcesSetAdapter rsa, final Log log) throws MojoExecutionException {
		InputStreamReader in = null;
		OutputStreamWriter out = null;

		try {
			if (rsa.getAggregation() == null) {
				// no aggregation
				for (File file : rsa.getFiles()) {
					log.info("Optimize CSS file " + file.getName() + " ...");
					addToOriginalSize(file);

					in = new InputStreamReader(new FileInputStream(file), rsa.getEncoding());

					// generate output
					String path = file.getCanonicalPath();
					if (StringUtils.isNotBlank(rsa.getSuffix())) {
						// create a new output stream
						File outputFile = getFileWithSuffix(path, rsa.getSuffix());
						out = new OutputStreamWriter(new FileOutputStream(outputFile), rsa.getEncoding());

						// compress and write compressed content into the new file
						CssCompressor compressor = new CssCompressor(in);
						compressor.compress(out, 500);
						closeStreams(in, out);

						// statistic
						addToOptimizedSize(outputFile);
					} else {
						// path of temp. file
						String pathOptimized = FileUtils.removeExtension(path) + OPTIMIZED_FILE_EXTENSION;

						// create a new temp. file and output stream
						File outputFile = new File(pathOptimized);
						Files.touch(outputFile);
						out = new OutputStreamWriter(new FileOutputStream(outputFile), rsa.getEncoding());

						// compress and write compressed content into the new file
						CssCompressor compressor = new CssCompressor(in);
						compressor.compress(out, 500);
						closeStreams(in, out);

						// rename the new file (overwrite the original file)
						FileUtils.rename(outputFile, file);

						// statistic
						addToOptimizedSize(file);
					}
				}
			} else if (rsa.getAggregation().getOutputFile() != null) {
				// aggregation to one output file
				File outputFile;
				Charset cset = Charset.forName(rsa.getEncoding());

				if (!rsa.getAggregation().isWithoutCompress()) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					OutputStreamWriter osw = new OutputStreamWriter(baos, rsa.getEncoding());

					// with compressing before aggregation
					for (File file : rsa.getFiles()) {
						log.info("Optimize CSS file " + file.getName() + " ...");
						addToOriginalSize(file);

						// create input stream for the current file
						in = new InputStreamReader(new FileInputStream(file), rsa.getEncoding());

						// compress and write compressed content into the output stream
						CssCompressor compressor = new CssCompressor(in);
						compressor.compress(osw, 500);

						// close stream
						IOUtil.close(in);
					}

					// close stream
					IOUtil.close(osw);

					int filesCount = rsa.getFiles().size();
					if (rsa.getAggregation().getPrependedFile() != null) {
						filesCount++;
					}

					if (filesCount > 1) {
						log.info("Aggregation is running ...");
					}

					// get right output file
					outputFile = getOutputFile(rsa);

					long sizeBefore = outputFile.length();

					if (rsa.getAggregation().getPrependedFile() != null) {
						// write / append to be prepended file into / to the output file
						prependFile(rsa.getAggregation().getPrependedFile(), outputFile, cset, rsa.getEncoding());
					}

					// write / append compiled content into / to the output file
					Files.append(baos.toString(rsa.getEncoding()), outputFile, cset);

					// statistic
					addToOptimizedSize(outputFile.length() - sizeBefore);

					if (filesCount > 1) {
						log.info(filesCount + " files were successfully aggregated.");
					}
				} else {
					// only aggregation without compressing
					outputFile = aggregateFiles(rsa, cset, log);
				}

				// delete single files if necessary
				deleteFilesIfNecessary(rsa, log);

				// rename aggregated file if necessary
				renameOutputFileIfNecessary(rsa, outputFile);
			} else {
				// should not happen
				log.error("Wrong plugin's internal state.");
			}
		} catch (Exception e) {
			throw new MojoExecutionException("Resources optimization failure: " + e.getLocalizedMessage(), e);
		} finally {
			closeStreams(in, out);
		}
	}

	protected void closeStreams(final Reader in, final Writer out) {
		IOUtil.close(in);
		IOUtil.close(out);
	}
}
