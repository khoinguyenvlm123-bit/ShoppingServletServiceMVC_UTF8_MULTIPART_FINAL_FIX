package vn.iotstar.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.util.UploadConstant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet("/image")
public class DownloadImageController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fileName = req.getParameter("fname");
        if (fileName == null || fileName.isBlank() || fileName.contains("..")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        File base = new File(UploadConstant.DIR).getCanonicalFile();
        File file = new File(base, fileName.replace('/', File.separatorChar)).getCanonicalFile();
        if (!file.toPath().startsWith(base.toPath()) || !file.exists() || !file.isFile()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String type = getServletContext().getMimeType(file.getName());
        resp.setContentType(type == null ? "application/octet-stream" : type);
        resp.setContentLengthLong(file.length());
        try (FileInputStream input = new FileInputStream(file)) {
            input.transferTo(resp.getOutputStream());
        }
    }
}
