
import Compressor from "compressorjs";
export class FileService {
  
  async comprimirImagem(imagem: Blob, resolve: Function) {

    await new Compressor( imagem, {
      quality: 0.6, // Adjust the desired image quality (0.0 - 1.0)
      maxWidth: 800, // Adjust the maximum width of the compressed image
      maxHeight: 800, // Adjust the maximum height of the compressed image
      mimeType: "image/jpeg", // Specify the output image format
      success(result) {
        resolve(result)
      }, 
    });
  }
}