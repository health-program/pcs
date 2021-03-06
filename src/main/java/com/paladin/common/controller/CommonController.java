package com.paladin.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.paladin.common.core.ConstantsContainer;
import com.paladin.common.core.ConstantsContainer.KeyValue;
import com.paladin.common.model.syst.SysAttachment;
import com.paladin.common.service.syst.SysAttachmentService;
import com.paladin.framework.common.OffsetPage;
import com.paladin.framework.core.VersionContainerManager;
import com.paladin.framework.core.exception.BusinessException;
import com.paladin.framework.core.session.UserSession;
import com.paladin.framework.web.response.CommonResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api("通用操作管理")
@Controller
@RequestMapping("/common")
public class CommonController {

	@Autowired
	private SysAttachmentService attachmentService;

	@ApiOperation(value = "通过code获取常量", response = KeyValue.class, responseContainer = "List")
	@ApiImplicitParam(name = "code", value = "CODE", required = true, dataType = "String", allowMultiple = true)
	@GetMapping("/constant")
	@ResponseBody
	public Object enumConstants(@RequestParam("code") String[] code) {
		return CommonResponse.getSuccessResponse(ConstantsContainer.getTypeChildren(code));
	}

	@ApiOperation(value = "通过ID获取附件", response = SysAttachment.class)
	@ApiImplicitParam(name = "id", value = "附件ID", required = true, dataType = "String", paramType = "path")
	@GetMapping("/attachment/{id}")
	@ResponseBody
	public Object getAttachment(@PathVariable("id") String id) {
		return CommonResponse.getSuccessResponse(attachmentService.get(id));
	}

	@ApiOperation(value = "通过ID获取多个附件", response = SysAttachment.class, responseContainer = "List")
	@ApiImplicitParam(name = "ids", value = "ID数组", required = true, dataType = "String", allowMultiple = true)
	@GetMapping("/attachment")
	@ResponseBody
	public Object getAttachments(@RequestParam("id[]") String[] ids) {
		return CommonResponse.getSuccessResponse(attachmentService.getAttachment(ids));
	}

	@ApiOperation(value = "分页查询图片附件", response = SysAttachment.class, responseContainer = "List")
	@ApiImplicitParam(name = "query", value = "查询条件", dataType = "OffsetPage")
	@GetMapping("/resource/images")
	@ResponseBody
	public Object getImageResource(OffsetPage query) {
		return CommonResponse.getSuccessResponse(attachmentService.getResourceImagePage(query));
	}

	@ApiOperation(value = "上传附件文件", response = SysAttachment.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "file", value = "文件", dataType = "file", required = true),
			@ApiImplicitParam(name = "filename", value = "文件名称"), @ApiImplicitParam(name = "userType", value = "附件关联类型") })
	@PostMapping("/upload/file")
	@ResponseBody
	public Object uploadFile(@RequestParam("file") MultipartFile file, @RequestParam(name = "filename", required = false) String name,
			@RequestParam(name = "userType", required = false) Integer userType) {
		return CommonResponse.getSuccessResponse(attachmentService.createAttachment(file, name, userType));
	}

	@ApiOperation(value = "上传多个附件文件", response = SysAttachment.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "file", value = "文件", dataType = "file", required = true, allowMultiple = true),
			@ApiImplicitParam(name = "filename", value = "文件名称", allowMultiple = true),
			@ApiImplicitParam(name = "userType", value = "附件关联类型", allowMultiple = true) })
	@PostMapping("/upload/files")
	@ResponseBody
	public Object uploadFiles(@RequestParam("files") MultipartFile[] files, @RequestParam(value = "filename", required = false) String[] names,
			@RequestParam(name = "userType", required = false) Integer userType) {
		SysAttachment[] result = new SysAttachment[files.length];
		for (int i = 0; i < files.length; i++) {
			MultipartFile file = files[i];
			String filename = (names != null && names.length > i) ? names[i] : null;
			result[i] = attachmentService.createAttachment(file, filename, userType);
		}
		return CommonResponse.getSuccessResponse(result);
	}

	@ApiOperation(value = "上传base64格式的附件文件", response = SysAttachment.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "fileStr", value = "base64格式字符串", required = true), @ApiImplicitParam(name = "filename", value = "文件名称"),
			@ApiImplicitParam(name = "fileType", value = "文件后缀"), @ApiImplicitParam(name = "userType", value = "附件关联类型") })
	@PostMapping("/upload/file/base64")
	@ResponseBody
	public Object uploadFileByBase64(@RequestParam String fileStr, @RequestParam(required = false) String filename,
			@RequestParam(required = false) String fileType, @RequestParam(required = false) Integer userType) {
		if (fileStr == null || fileStr.length() == 0) {
			return CommonResponse.getErrorResponse("上传文件为空");
		}
		SysAttachment result = attachmentService.createAttachment(fileStr, filename == null || filename.length() == 0 ? "附件" : filename, fileType, userType);
		return CommonResponse.getSuccessResponse(result);
	}

	@ApiOperation(value = "上传图片，图片过大会被压缩", response = SysAttachment.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "picture", value = "图片", required = true), @ApiImplicitParam(name = "pictureName", value = "图片名称"),
			@ApiImplicitParam(name = "userType", value = "附件关联类型"), @ApiImplicitParam(name = "thumbnailWidth", value = "缩略图宽度"),
			@ApiImplicitParam(name = "thumbnailHeight", value = "缩略图高度") })
	@PostMapping("/upload/picture")
	@ResponseBody
	public Object uploadPicture(@RequestParam MultipartFile picture, @RequestParam(required = false) String pictureName,
			@RequestParam(required = false) Integer userType, @RequestParam(required = false) Integer thumbnailWidth,
			@RequestParam(required = false) Integer thumbnailHeight) {
		SysAttachment result = attachmentService.createPictureAndCompress(picture, pictureName, userType, thumbnailWidth, thumbnailHeight);
		return CommonResponse.getSuccessResponse(result);
	}

	@ApiOperation(value = "上传base64格式图片，图片过大会被压缩", response = SysAttachment.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "pictureStr", value = "图片base64字符串", required = true),
			@ApiImplicitParam(name = "pictureName", value = "图片名称"), @ApiImplicitParam(name = "pictureType", value = "图片类型"),
			@ApiImplicitParam(name = "userType", value = "附件关联类型"), @ApiImplicitParam(name = "thumbnailWidth", value = "缩略图宽度"),
			@ApiImplicitParam(name = "thumbnailHeight", value = "缩略图高度") })
	@PostMapping("/upload/picture/base64")
	@ResponseBody
	public Object uploadPictureBase6(@RequestParam String pictureStr, @RequestParam(required = false) String pictureName,
			@RequestParam(required = false) String pictureType, @RequestParam(required = false) Integer userType,
			@RequestParam(required = false) Integer thumbnailWidth, @RequestParam(required = false) Integer thumbnailHeight) {
		SysAttachment result = attachmentService.createPictureAndCompress(pictureStr, pictureName, pictureType, userType, thumbnailWidth, thumbnailHeight);
		return CommonResponse.getSuccessResponse(result);
	}

	@ApiOperation(value = "重启容器")
	@ApiImplicitParam(name = "container", value = "容器ID", required = true)
	@GetMapping("/restart/container")
	@ResponseBody
	public Object restartContainer(@RequestParam String container) {
		if (UserSession.getCurrentUserSession().isSystemAdmin()) {
			VersionContainerManager.versionChanged(container);
			return CommonResponse.getSuccessResponse();
		}
		return CommonResponse.getNoPermissionResponse("无权限");
	}

	@ApiOperation(value = "重启所有容器")
	@GetMapping("/restart/container/all")
	@ResponseBody
	public Object restartAllContainer() {
		if (UserSession.getCurrentUserSession().isSystemAdmin()) {
			VersionContainerManager.versionChanged();
			return CommonResponse.getSuccessResponse();
		}
		return CommonResponse.getNoPermissionResponse("无权限");
	}

	@ApiOperation(value = "异常测试")
	@GetMapping("/test/exception")
	public Object getException() {
		throw new BusinessException("异常测试，返回页面");
	}

	@ApiOperation(value = "异常测试")
	@PostMapping("/test/exception")
	@ResponseBody
	public Object postException() {
		throw new BusinessException("异常测试，返回json");
	}

}
