import os
import sys

def doi_ten_java_sang_txt():
    """
    Tìm tất cả các tệp .java trong thư mục hiện tại của script
    và đổi phần mở rộng của chúng thành .txt.
    """
    try:
        # Lấy đường dẫn thư mục chứa script này
        # __file__ là biến đặc biệt chứa đường dẫn đến tệp script đang chạy
        script_dir = os.path.dirname(os.path.abspath(__file__))
    except NameError:
        # Xảy ra khi chạy trong môi trường không định nghĩa __file__ (ví dụ: interactive shell)
        # Trong trường hợp này, lấy thư mục làm việc hiện tại làm mặc định
        print("Không thể xác định thư mục script, sử dụng thư mục làm việc hiện tại.")
        script_dir = os.getcwd() # Lấy thư mục làm việc hiện tại

    print(f"Đang quét thư mục: {script_dir}")

    files_renamed_count = 0
    files_skipped_count = 0

    # Lặp qua tất cả các tệp và thư mục con trong thư mục script
    for filename in os.listdir(script_dir):
        # Tạo đường dẫn đầy đủ tới tệp/thư mục
        old_file_path = os.path.join(script_dir, filename)

        # Kiểm tra xem có phải là TỆP và có kết thúc bằng .java không
        if os.path.isfile(old_file_path) and filename.lower().endswith(".java"):
            # Tách tên tệp và phần mở rộng
            base_name = os.path.splitext(filename)[0]
            new_filename = base_name + ".txt"
            new_file_path = os.path.join(script_dir, new_filename)

            # Kiểm tra xem tệp .txt mới đã tồn tại chưa
            if os.path.exists(new_file_path):
                print(f"(!) Cảnh báo: Tệp '{new_filename}' đã tồn tại. Bỏ qua đổi tên '{filename}'.")
                files_skipped_count += 1
                continue # Chuyển sang tệp tiếp theo

            # Thực hiện đổi tên
            try:
                os.rename(old_file_path, new_file_path)
                print(f" -> Đã đổi tên: '{filename}' thành '{new_filename}'")
                files_renamed_count += 1
            except OSError as e:
                print(f"[LỖI] Không thể đổi tên '{filename}': {e}")
                files_skipped_count += 1

    # In thông báo tổng kết
    print("\n--- Hoàn tất ---")
    if files_renamed_count > 0:
        print(f"Đã đổi tên thành công {files_renamed_count} tệp.")
    if files_skipped_count > 0:
         print(f"Đã bỏ qua {files_skipped_count} tệp (do lỗi hoặc tệp đích đã tồn tại).")
    if files_renamed_count == 0 and files_skipped_count == 0:
        print("Không tìm thấy tệp .java nào để đổi tên trong thư mục này.")

# Chỉ chạy hàm khi script được thực thi trực tiếp
if __name__ == "__main__":
    doi_ten_java_sang_txt()